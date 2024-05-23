package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.ClientError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.example.example.Kitchen
import com.google.gson.Gson
import com.mobile.cloudkitchen.ui.adapter.KitchenMealsAdapter
import com.mobile.cloudkitchen.databinding.FragmentKitchenDetailsBinding
import com.mobile.cloudkitchen.data.model.MealsModel
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject


class KitchenDetailsFragment : Fragment(), ServiceResponse {
    private var _binding: FragmentKitchenDetailsBinding? = null
    private val binding get() = _binding!!
    private var mealsModelList = ArrayList<MealsModel>()
    lateinit var sp: SharedPreferences
    private lateinit var kitchenMealsAdapter: KitchenMealsAdapter
    private var mealId: String = ""
    private lateinit var monthSubAmount: String
    private lateinit var wkSubAmount: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentKitchenDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.rvMeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val args = arguments
        val kitchenId = args?.getString("kitchen_id", "0")
        val kitchenTitle = args?.getString("kitchen_title", "")
        val kitchenDesc = args?.getString("kitchen_desc", "")
        val kitchenType = args?.getString("kitchen_type", "")
        binding.kitchenTitleTxt.text = kitchenTitle
        binding.kitchenDesc.text = kitchenDesc
        if (kitchenType?.contains("veg") == true)
            binding.vegCard.visibility = View.VISIBLE
        if (kitchenType?.contains("non") == true)
            binding.nonvegCard.visibility = View.VISIBLE

        kitchenDetailsViewModel.callKitchenDetails()

        APIService.makeKitchenDetailsAPICall(
            requireActivity(),
            this,
            "kitchens/$kitchenId",
            sp.getString("TOKEN", "NA")
        )
        _binding!!.viewPlanLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", kitchenId)
            bundle.putString("meal_id", mealId)
            bundle.putString("month", monthSubAmount)
            bundle.putString("wk", wkSubAmount)
            (requireActivity() as HomeActivity?)?.loadFragment(SelectPlanFragment(), bundle)
        }
        _binding?.backIcon?.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false, false)
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)

    }


    override fun onSuccessResponse(response: Any?, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        if (tag.toString().contains("kitchens")) {
            var jsonObject = JSONObject(response as String)
            val gson = Gson()
            val kitchen: Kitchen = gson.fromJson(
                response.toString(),
                Kitchen::class.java
            )
            mealId = kitchen.Id.toString()
            monthSubAmount = kitchen.meals[0].monthlySubscriptionCost.toString()
            wkSubAmount = kitchen.meals[0].weeklySubscriptionCost.toString()
            GlobalScope.launch(Dispatchers.Main) {
                UserUtils.setKitchen(kitchen)
                kitchenMealsAdapter = KitchenMealsAdapter(requireActivity(), kitchen)
                binding.rvMeals.adapter = kitchenMealsAdapter
                binding.selectedMealTxt.text = kitchen.meals[0].name
                binding.viewPlanLayout.isClickable = true
            }
        }
    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        if (error is ClientError) {
            var msg = (JSONObject(
                String(
                    error.networkResponse.data,
                    charset("UTF-8")
                )
            ).getString("error"))
            Toast.makeText(
                requireActivity(),
                msg,
                Toast.LENGTH_LONG
            ).show();
        } else if (error is NetworkError) {
            Toast.makeText(
                requireActivity(),
                "NetworkError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ServerError) {
            Toast.makeText(
                requireActivity(),
                "ServerError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is AuthFailureError) {
            Toast.makeText(
                requireActivity(),
                "AuthFailureError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ParseError) {
            Toast.makeText(
                requireActivity(),
                "ParseError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is NoConnectionError) {
            Toast.makeText(
                requireActivity(),
                "NoConnectionError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is TimeoutError) {
            Toast.makeText(
                requireActivity(),
                "Oops. Timeout error!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ClientError) {
            Toast.makeText(
                requireActivity(),
                "Oops. ClientError",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.showHideBottomNavigation(true, true)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(mealsModel: MealsModel) {
        this.mealId = mealsModel._id
        binding.selectedMealTxt.text = mealsModel.title
        monthSubAmount = mealsModel.monthlySubscriptionPrice.toString()
        wkSubAmount = mealsModel.wklySubscriptionPrice.toString()
    }
}