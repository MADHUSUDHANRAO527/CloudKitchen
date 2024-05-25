package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.example.Kitchen
import com.example.example.User
import com.google.gson.Gson
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentPlaceOrderBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.ui.adapter.KitchenMealsAdapter
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class PlaceOrderFragment : Fragment(), ServiceResponse {
    private var _binding: FragmentPlaceOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var planType: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.addressTxt.text = sp.getString("SELECTED_ADDRESS","Click here to add address!").toString().split("*")[1]
        val onlyMealCost = requireActivity().resources.getString(R.string.Rs) + UserUtils.getReviewOrder().totalAmount.toString()
        val mealsCostTxt =  onlyMealCost +
                "(" + if (UserUtils.planType.contains("W")) UserUtils.getKitchen().availablePlans[0].noOfMeals.toString() else UserUtils.getKitchen().availablePlans[1].noOfMeals.toString()
        val savedAmountTxt = requireActivity().resources.getString(R.string.Rs) + UserUtils.getReviewOrder().savedAmount.toString()
        binding.planTypeBtn.text = if (UserUtils.planType.contains("W")) "WEEKLY" else "MONTHLY"
        binding.mealsCostTxt.text = mealsCostTxt
        binding.totalSavingTxt.text =savedAmountTxt
        binding.subscriptionCostTxt.text = onlyMealCost

        binding.deliveryChargesTxt.text =
            requireActivity().resources.getString(R.string.Rs) + "0.00"
        binding.grandTotalTxt.text =
            requireActivity().resources.getString(R.string.Rs) + UserUtils.getReviewOrder().grandTotal.toString()
        binding.mealTitleTxt.text = UserUtils.getMeal().name
        binding.mealTitle.text = UserUtils.getMeal().name
        binding.mealTitleDesc.text = UserUtils.getMeal().description
        binding.mealsCostTxt.text = onlyMealCost

        "${UserUtils.fromHumanDate}-${UserUtils.toHumanDate}".also { binding.planDurationTxt.text = it }
        UserUtils.timeSlot.also { binding.deliverySlotTxt.text = it }
        _binding!!.paynowBtn.setOnClickListener {
            AppUtils.showToast(requireActivity(),"will navigate to payment screen!")
        }
        binding.changeDurationTxt.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(SelectDurationFragment(), null)
        }
        binding.changeSubscription.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(SelectPlanFragment(), null)
        }
        binding.changeAddressTxt.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(LocationFragment(), null)
        }

        return root
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        //{"totalAmount":0,"plannedDates":[],"discount":"10%","grandTotal":0,"savedAmount":0}
        _binding?.pBar?.visibility = View.GONE
        if (tag.toString().contains("kitchens")) {

        }
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        AppUtils.showErrorMsg(error, tag.toString(), requireActivity())
    }

}