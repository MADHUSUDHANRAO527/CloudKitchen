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
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentPlaceOrderBinding
import com.mobile.cloudkitchen.ui.activity.HomeActivity


class PlaceOrderFragment : Fragment() {
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
        val kitchenId = args?.getString("kitchen_id", "0")
        val mealId = args?.getString("meal_id", "0")
        val monthSubAmount = args?.getString("month", "")
        val wkSubAmount = args?.getString("wk", "")



        _binding!!.placeOrderBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", kitchenId)
            bundle.putString("meal_id", mealId)
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

}