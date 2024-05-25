package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentSelectPlanBinding
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils


class SelectPlanFragment : Fragment() {
    private var _binding: FragmentSelectPlanBinding? = null
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

        _binding = FragmentSelectPlanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        val args = arguments
      /*  val kitchenId = args?.getString("kitchen_id", "0")
        val mealId = args?.getString("meal_id", "0")
        val monthSubAmount = args?.getString("month", "")
        val wkSubAmount = args?.getString("wk", "")*/

        binding.wkMealPriceTxt.text = UserUtils.wklyAmount
        binding.monthlyMealPriceTxt.text = UserUtils.monthlyAmount
        binding.monthlyNoOfmeals.text = "("+UserUtils.getKitchen().availablePlans[0].noOfMeals.toString() + " Meals)"
        binding.noOfmeals.text = "("+UserUtils.getKitchen().availablePlans[1].noOfMeals.toString()+" Meals)"

        planType = _binding!!.wklyPlan.tag.toString()
        _binding!!.wklyPlan.setOnClickListener {
            binding.wkSelectBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.wkSelectBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            binding.monthlySelectBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            binding.monthlySelectBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
            planType = _binding!!.wklyPlan.tag.toString()
        }
        _binding!!.monthlyPlan.setOnClickListener {
            binding.monthlySelectBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_btn_bg_shape));
            binding.monthlySelectBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            binding.wkSelectBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
            binding.wkSelectBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.plan_unselect_btn_bg_shape));
            planType = _binding!!.monthlyPlan.tag.toString()
        }
       // APIService.makeKitchenDetailsAPICall(requireActivity(), this, "kitchens/$kitchenId",sp.getString("TOKEN","NA"))
        _binding!!.selectMenuBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", UserUtils.getKitchen().Id)
            bundle.putString("meal_id", UserUtils.mealID)
            bundle.putString("plan_type", planType)
            UserUtils.planType = planType
            (requireActivity() as HomeActivity?)?.loadFragment(SelectDurationFragment(), bundle)
        }
        return root
    }

}