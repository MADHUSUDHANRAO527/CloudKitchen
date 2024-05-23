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
import com.google.gson.Gson
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


class PlaceOrderFragment : Fragment(),ServiceResponse {
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
        val args = arguments
      /*  val kitchenId = args?.getString("kitchen_id", "0")
        val mealId = args?.getString("meal_id", "0")
        val monthSubAmount = args?.getString("month", "")
        val wkSubAmount = args?.getString("wk", "")*/

        APIService.process(
            requireActivity(),
            this ,"/orders/processOrder"
        )

        _binding!!.placeOrderBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", UserUtils.getKitchen().Id)
            bundle.putString("meal_id", UserUtils.mealID)
            bundle.putString("plan_type", planType)
            (requireActivity() as HomeActivity?)?.loadFragment(ViewMenuFragment(), bundle)
        }

        return root
    }

    private fun showDatePopup(startDate: Boolean, endDate: Boolean) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(com.mobile.cloudkitchen.R.layout.calender_layout, null)

        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
            context
        )
        val calender = promptsView
            .findViewById<View>(com.mobile.cloudkitchen.R.id.calender) as CalendarView
        alertDialogBuilder.setView(promptsView)
        val alertDialog: android.app.AlertDialog? = alertDialogBuilder.create()

        calender.setOnDateChangeListener { view, year, month, dayOfMonth ->  val Date = (dayOfMonth.toString() + "-"
                + (month + 1) + "-" + year)

            // set this date in TextView for Display
            if(startDate) binding.startDateTxt.text = Date else if(endDate) binding.endDateTxt.text = Date
            alertDialog?.dismiss()
        }
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id -> // get user input and set it to result
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        alertDialog!!.show()
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
           /* mealId = kitchen.Id.toString()
            monthSubAmount = kitchen.meals[0].monthlySubscriptionCost.toString()
            wkSubAmount = kitchen.meals[0].weeklySubscriptionCost.toString()
            GlobalScope.launch(Dispatchers.Main) {
                UserUtils.setKitchen(kitchen)
                kitchenMealsAdapter = KitchenMealsAdapter(requireActivity(), kitchen)
                binding.rvMeals.adapter = kitchenMealsAdapter
                binding.selectedMealTxt.text = kitchen.meals[0].name
                binding.viewPlanLayout.isClickable = true
            }*/
        }
    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        AppUtils.showErrorMsg(error,tag.toString(),requireActivity())
    }

}