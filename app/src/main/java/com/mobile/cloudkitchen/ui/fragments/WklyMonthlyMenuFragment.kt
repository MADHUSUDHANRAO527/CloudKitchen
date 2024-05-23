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
import com.mobile.cloudkitchen.ui.adapter.WkMonthMealsMenuAdapter
import com.mobile.cloudkitchen.databinding.FragmentWklyMonthlyMenuBinding
import com.mobile.cloudkitchen.data.model.MealsModel
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WklyMonthlyMenuFragment(mPosition: Int,mPlanType:String) : Fragment() {
    private var _binding: FragmentWklyMonthlyMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mealsModelList = ArrayList<MealsModel>()
    lateinit var sp: SharedPreferences
    private lateinit var kitchenMealsAdapter: WkMonthMealsMenuAdapter
    private var position  = mPosition
    private var planType  = mPlanType
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentWklyMonthlyMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        //   binding.rvMeals.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val args = arguments
        val kitchenId = args?.getString("kitchen_id", "0")
        val mealId = args?.getString("meal_id", "0")
        binding.rvMeals.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        GlobalScope.launch(Dispatchers.Main) {

            kitchenMealsAdapter = WkMonthMealsMenuAdapter(requireActivity(), planType,if(AppUtils.monthlyMenuList.isNotEmpty()) AppUtils.monthlyMenuList.get(position) else AppUtils.wklyMenuList.get(position))
            binding.rvMeals.adapter = kitchenMealsAdapter
         //   binding.selectedMealTxt.text = mealsModelList[0].name
        }


        // APIService.makeKitchenDetailsAPICall(requireActivity(), this, "kitchens/$kitchenId",sp.getString("TOKEN","NA"))
       /* _binding!!.selectMenuBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", kitchenId)
            bundle.putString("meal_id", mealId)
            (requireActivity() as HomeActivity?)?.loadFragment(ViewMenuFragment(), bundle)
        }*/
        return root
    }

}