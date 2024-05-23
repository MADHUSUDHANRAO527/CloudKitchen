package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentSelectDurationBinding
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class SelectDurationFragment : Fragment() {
    private var _binding: FragmentSelectDurationBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private  var planType: String =""
    var selectedStartDate = Calendar.getInstance()
    var selectedEndDate = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentSelectDurationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        val args = arguments
        val kitchenId = args?.getString("kitchen_id", "0")
        val mealId = args?.getString("meal_id", "0")
        planType = args?.getString("plan_type").toString()
        val monthSubAmount = args?.getString("month", "")
        val wkSubAmount = args?.getString("wk", "")
        if(planType.contains("M")){
            UserUtils.planId = UserUtils.getKitchen().availablePlans[0].Id.toString()
        }else{
            UserUtils.planId = UserUtils.getKitchen().availablePlans[1].Id.toString()
        }

            binding.startDateTxt.text =AppUtils.getCurrentDate()
        binding.endDateTxt.text =AppUtils.getCurrentDate()

        binding.startDate.setOnClickListener {
            showDatePopup(true,false)
        }
        binding.endDate.setOnClickListener {
            showDatePopup(false,true)
        }
        UserUtils.timeSlot = binding.selectBtnFirstSlot.tag.toString()

        _binding!!.selectBtnFirstSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnFirstSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.selectBtnFirstSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            binding.selectBtnTwoSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnTwoSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
            binding.selectBtnThreeSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnThreeSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
            binding.selectBtnFourthSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFourthSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        }
        _binding!!.selectBtnTwoSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnTwoSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFirstSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnTwoSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.selectBtnTwoSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            binding.selectBtnThreeSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnThreeSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
            binding.selectBtnFourthSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFourthSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        }
        _binding!!.selectBtnThreeSlot.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnThreeSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFirstSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnTwoSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnTwoSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnThreeSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.selectBtnThreeSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            binding.selectBtnFourthSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFourthSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        }
        _binding!!.selectBtnFourthSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnFourthSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnFirstSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnTwoSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnTwoSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnThreeSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.selectBtnThreeSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary));
            binding.selectBtnFourthSlot.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.selectBtnFourthSlot.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        }

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
        calender.setMinDate(currentTimeMillis() -1000)



        calender.setOnDateChangeListener { view, year, month, dayOfMonth ->  val Date = (dayOfMonth.toString() + "-"
                + (month + 1) + "-" + year)
            if (startDate)
                selectedStartDate.set(year, month + 1, dayOfMonth)
            else
                selectedEndDate.set(year, month + 1, dayOfMonth)

            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            val cal_Two: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            println(cal_Two.time)

            // set this date in TextView for Display
            if(startDate) {
                binding.startDateTxt.text = Date
            }else if(endDate) {
                binding.endDateTxt.text = Date
            }
            val msDiff: Long = selectedEndDate.timeInMillis - selectedStartDate.timeInMillis
            val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)
            if(planType.contains("M") && daysDiff>30){
                Toast.makeText(
                    requireActivity(),
                    "Your monthly plan should not cross 30 days!",
                    Toast.LENGTH_LONG
                ).show();
            }else if (planType.contains("W") && daysDiff>7){
                Toast.makeText(
                    requireActivity(),
                    "Your weekly plan should not cross 7 days!",
                    Toast.LENGTH_LONG
                ).show();
            }else{
                val startDate = Date( selectedStartDate.timeInMillis)
                val endDate = Date( selectedStartDate.timeInMillis)
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                 val startSimpleDateFormat =simpleDateFormat.format(startDate)
                 val endSimpleDateFormat =simpleDateFormat.format(endDate)
                UserUtils.fromDate = startSimpleDateFormat
                UserUtils.toDate = endSimpleDateFormat
                alertDialog?.dismiss()
            }
        }
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("OK",
                { dialog, id -> // get user input and set it to result
                })
            .setNegativeButton("Cancel",
                { dialog, id -> dialog.cancel() })
        alertDialog!!.show()
    }

}