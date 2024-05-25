package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.example.example.Kitchen
import com.example.example.Meals
import com.example.example.User
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mobile.cloudkitchen.ui.adapter.KitchenMealsAdapter
import com.mobile.cloudkitchen.databinding.FragmentKitchenDetailsBinding
import com.mobile.cloudkitchen.data.model.MealsModel
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class KitchenDetailsFragment : Fragment(), ServiceResponse,KitchenMealsAdapter.AdapterCallback {
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

       // kitchenDetailsViewModel.callKitchenDetails()

        APIService.makeKitchenDetailsAPICall(
            requireActivity(),
            this,
            "kitchens/$kitchenId",
        )
        _binding!!.viewPlanLayout.setOnClickListener {
             UserUtils.monthlyAmount = monthSubAmount
             UserUtils.wklyAmount = wkSubAmount
            (requireActivity() as HomeActivity?)?.loadFragment(SelectPlanFragment(), null)
        }
        _binding?.backIcon?.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false, false)
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        if (tag.toString().contains("kitchens")) {
            var jsonObject = JSONObject(response as String)
            val gson = Gson()
            var kitchen = Kitchen()
            try {
                kitchen = gson.fromJson(
                    response.toString(),
                    Kitchen::class.java
                )
            } catch (e: JsonSyntaxException) {
                AppUtils.showToast(requireActivity(), e.message.toString()+"-response from server side!")
                binding.viewPlanLayout.visibility = View.GONE
                return
            }
            if (kitchen.Id != null&&kitchen.meals.isNotEmpty()) {
                mealId = kitchen.Id.toString()
                monthSubAmount = kitchen.meals[0].monthlySubscriptionCost.toString()
                wkSubAmount = kitchen.meals[0].weeklySubscriptionCost.toString()
             //   GlobalScope.launch(Dispatchers.Main) {
                    UserUtils.setKitchen(kitchen)
                    kitchenMealsAdapter = KitchenMealsAdapter(requireActivity(), kitchen,this)
                    binding.rvMeals.adapter = kitchenMealsAdapter
                    binding.selectedMealTxt.text = kitchen.meals[0].name
                    binding.viewPlanLayout.isClickable = true
             //   }
            }else{
                AppUtils.showToast(requireActivity(),"No meals found for this kitchen!")
                binding.viewPlanLayout.visibility = View.GONE
            }
        }
    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        AppUtils.showErrorMsg(error= VolleyError(),tag = String(),requireActivity())
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.showHideBottomNavigation(true, true)
    }


    override fun itemClick(mealsModel: Meals) {
        this.mealId = mealsModel.Id.toString()
        binding.selectedMealTxt.text = mealsModel.name
        monthSubAmount = mealsModel.monthlySubscriptionCost.toString()
        wkSubAmount = mealsModel.weeklySubscriptionCost.toString()
    }
}