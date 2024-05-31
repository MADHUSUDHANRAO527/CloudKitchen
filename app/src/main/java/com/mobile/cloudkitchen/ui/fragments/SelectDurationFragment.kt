package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.ReviewOrder
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
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private var planType: String = ""
    var selectedStartDate = Calendar.getInstance()
    var selectedEndDate = Calendar.getInstance()
    var tomoDate : String = ""
    var afterAMonthWkDate : String = ""
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
        planType = args?.getString("plan_type").toString()
        tomoDate = AppUtils.getTomoDate(true)
        afterAMonthWkDate = AppUtils.getTomoDate(false)


        //setting to Utils - from date & to date if not selected by user
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        //prepare human readable format --31 May 2024
        val sdf = SimpleDateFormat("dd MMMM yyyy ")
        val calendar = Calendar.getInstance()
        calendar[tomoDate.split("-")[2].toInt(), tomoDate.split("-")[1].toInt()-1] = tomoDate.split("-")[0].toInt()
       // if (startDate) {
        selectedStartDate.set(
            tomoDate.split("-")[2].toInt(),
            tomoDate.split("-")[1].toInt() - 1,
            tomoDate.split("-")[0].toInt()
        )

            val startDate = Date(selectedStartDate.timeInMillis)
            val startSimpleDateFormat = simpleDateFormat.format(startDate)
            UserUtils.fromDate = startSimpleDateFormat
            //store human readable format --31 May 2024
            val sDate = sdf.format(calendar.time)
            UserUtils.fromHumanDate = sDate
      //  } else {
        selectedEndDate.set(
            afterAMonthWkDate.split("-")[2].toInt(),
            afterAMonthWkDate.split("-")[1].toInt(),
            afterAMonthWkDate.split("-")[0].toInt()
        )
        calendar[afterAMonthWkDate.split("-")[2].toInt(), afterAMonthWkDate.split("-")[1].toInt()-1] = afterAMonthWkDate.split("-")[0].toInt()

        val endDate = Date(selectedEndDate.timeInMillis)
            val endSimpleDateFormat = simpleDateFormat.format(endDate)
            UserUtils.toDate = endSimpleDateFormat
            val eDate = sdf.format(calendar.time)
            UserUtils.toHumanDate = eDate
      //  }







        if (planType.contains("M")) {
            UserUtils.planId = UserUtils.getKitchen().availablePlans[0].Id.toString()
        } else {
            UserUtils.planId = UserUtils.getKitchen().availablePlans[1].Id.toString()
        }

        binding.startDateTxt.text = tomoDate
        binding.endDateTxt.text = afterAMonthWkDate
       /* selectedEndDate.set(
            afterAMonthWkDate.split("-")[2].toInt(),
            afterAMonthWkDate.split("-")[1].toInt(),
            afterAMonthWkDate.split("-")[0].toInt()
        )*/
        binding.startDate.setOnClickListener {
            showDatePopup(true, false)
        }
        binding.endDate.setOnClickListener {
            showDatePopup(false, true)
        }
        selectSlotListeners()

        _binding!!.placeOrderBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", UserUtils.getKitchen().Id)
            bundle.putString("meal_id", UserUtils.mealID)
            bundle.putString("plan_type", planType)
            (requireActivity() as HomeActivity?)?.loadFragment(PlaceOrderFragment(), bundle)
        }

        return root
    }

    private fun selectSlotListeners() {
        UserUtils.timeSlot = binding.selectBtnFirstSlot.tag.toString()
        binding.selectBtnFirstSlot.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.plan_btn_bg_shape
            )
        );
        binding.selectBtnFirstSlot.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        );
        _binding!!.selectBtnFirstSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnFirstSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_btn_bg_shape
                )
            );
            binding.selectBtnFirstSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            );
            binding.selectBtnTwoSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnTwoSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
            binding.selectBtnThreeSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnThreeSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
            binding.selectBtnFourthSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFourthSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
        }
        _binding!!.selectBtnTwoSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnTwoSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFirstSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnTwoSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_btn_bg_shape
                )
            );
            binding.selectBtnTwoSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            );
            binding.selectBtnThreeSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnThreeSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
            binding.selectBtnFourthSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFourthSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
        }
        _binding!!.selectBtnThreeSlot.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnThreeSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFirstSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnTwoSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnTwoSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnThreeSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_btn_bg_shape
                )
            );
            binding.selectBtnThreeSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            );
            binding.selectBtnFourthSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFourthSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
        }
        _binding!!.selectBtnFourthSlotLayout.setOnClickListener {
            UserUtils.timeSlot = binding.selectBtnFourthSlot.tag.toString()
            binding.selectBtnFirstSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnFirstSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnTwoSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnTwoSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnThreeSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_unselect_btn_bg_shape
                )
            );
            binding.selectBtnThreeSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.primary
                )
            );
            binding.selectBtnFourthSlot.setBackground(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.plan_btn_bg_shape
                )
            );
            binding.selectBtnFourthSlot.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            );
        }
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

        val calendarInstance = Calendar.getInstance()
        calendarInstance.add(Calendar.DATE,if(startDate) 1 else if(UserUtils.planType.contains("M")) 30 else 7)

       // val tomoDateVal = AppUtils.getTomoDate(false)
        calender.setMinDate(calendarInstance.timeInMillis)
        if (startDate)
            selectedStartDate.set(
                tomoDate.split("-")[2].toInt(),
                tomoDate.split("-")[1].toInt() - 1,
                tomoDate.split("-")[0].toInt()
            )
        else
            selectedEndDate.set(
                afterAMonthWkDate.split("-")[2].toInt(),
                afterAMonthWkDate.split("-")[1].toInt(),
                afterAMonthWkDate.split("-")[0].toInt()
            )



      //  calender.setDate(if (startDate) selectedStartDate.timeInMillis else selectedEndDate.timeInMillis)


        calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val Date = (dayOfMonth.toString() + "-"
                    + (month + 1) + "-" + year)
            if (startDate)
                selectedStartDate.set(year, month + 1, dayOfMonth)
            else
                selectedEndDate.set(year, month + 1, dayOfMonth)

            // set this date in TextView for Display
            if (startDate) {
                binding.startDateTxt.text = Date
            } else if (endDate) {
                binding.endDateTxt.text = Date
            }
            val msDiff: Long = selectedEndDate.timeInMillis - selectedStartDate.timeInMillis
            val daysDiff: Long = TimeUnit.MILLISECONDS.toDays(msDiff)
           /* if (planType.contains("M") && daysDiff < 30) {
                AppUtils.showToast(
                    requireActivity(),
                    "Your monthly plan should be more than 30 days!"
                )
            } else */
            if (planType.contains("M") && daysDiff > 60) {
                AppUtils.showToast(requireActivity(), "Your monthly plan should less than 60 days!")
            } /*else if (planType.contains("W") && daysDiff > 14) {
                Toast.makeText(
                    requireActivity(),
                    "Your weekly plan should not cross 14 days!",
                    Toast.LENGTH_LONG
                ).show();
            }*/ else if (planType.contains("W") && daysDiff > 14) {
                Toast.makeText(
                    requireActivity(),
                    "Your weekly plan should be less than 14 days!",
                    Toast.LENGTH_LONG
                ).show();
            } else {
                //prepare UTC  format
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                //prepare human readable format --31 May 2024
                val sdf = SimpleDateFormat("dd MMMM yyyy ")
                val calendar = Calendar.getInstance()
                calendar[year, month] = dayOfMonth
                if (startDate) {
                    val startDate = Date(selectedStartDate.timeInMillis)
                    val startSimpleDateFormat = simpleDateFormat.format(startDate)
                    UserUtils.fromDate = startSimpleDateFormat
                    //store human readable format --31 May 2024
                    val sDate = sdf.format(calendar.time)
                    UserUtils.fromHumanDate = sDate
                } else {
                    val endDate = Date(selectedEndDate.timeInMillis)
                    val endSimpleDateFormat = simpleDateFormat.format(endDate)
                    UserUtils.toDate = endSimpleDateFormat
                    val eDate = sdf.format(calendar.time)
                    UserUtils.toHumanDate = eDate
                }
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