package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mobile.cloudkitchen.ui.adapter.KitchenMealsAdapter
import com.mobile.cloudkitchen.ui.adapter.MenuTabAdapter
import com.mobile.cloudkitchen.databinding.FragmentViewMenuBinding

import com.mobile.cloudkitchen.data.model.MonthlyMenu
import com.mobile.cloudkitchen.data.model.ReviewOrder
import com.mobile.cloudkitchen.data.model.WeeklyMenu
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class ViewMenuFragment : Fragment(), ServiceResponse {
    private var _binding: FragmentViewMenuBinding? = null
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private var weeklyMenuList = ArrayList<WeeklyMenu>()
    private var monthlyMenuList = ArrayList<MonthlyMenu>()
    private var weeksList = ArrayList<String>()
    private lateinit var kitchenMealsAdapter: KitchenMealsAdapter
    private lateinit var planType: String
    private lateinit var weekly1MenuArray: JSONArray
    private lateinit var weekly2MenuArray: JSONArray
    private lateinit var weekly3MenuArray: JSONArray
    private lateinit var weekly4MenuArray: JSONArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentViewMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        _binding?.backIcon?.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }

        APIService.makeKitchenDetailsAPICall(
            requireActivity(),
            this,
            "meals/${UserUtils.mealID}",
        )

        _binding!!.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(_binding!!.menuTab))

        _binding!!.menuTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                _binding!!.viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        _binding!!.selectMenuBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", UserUtils.getKitchen().Id)
            //  bundle.putString("meal_id", UserUtils.mealID)
            (requireActivity() as HomeActivity?)?.loadFragment(SelectPlanFragment(), bundle)
        }
        return root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onSuccessResponse(response: Any?, tag: Any?) {
        var gson = Gson()
        if (tag.toString().contains("processOrder")) {
            val processOrder: ReviewOrder = gson.fromJson(
                response.toString(),
                ReviewOrder::class.java
            )
            UserUtils.setReviewOrder(processOrder)
        } else {
            var jsonObject = JSONObject(response as String)
            binding.mealTitleTxt.text = jsonObject.getString("name")
            binding.mealTitleDesc.text = jsonObject.getString("description")
            binding.mealRating.rating = jsonObject.getDouble("avgRating").toFloat()
            binding.ratingTxt.text = jsonObject.getInt("noOfRatings").toString().plus(
                if (jsonObject.getInt("noOfRatings") <= 1) (" Review")
                else (" Reviews")
            )
            val monthlyMenuObject = jsonObject.getJSONObject("monthlyMenu")
            val wklyMenuArray = jsonObject.getJSONArray("weeklyMenu")
            planType = "M"
            if (planType.contains("M")) {
                if (monthlyMenuObject.has("week1"))
                    weekly1MenuArray = monthlyMenuObject.getJSONArray("week1")

                if (monthlyMenuObject.has("week2")) weekly2MenuArray =
                    monthlyMenuObject.getJSONArray("week2")

                if (monthlyMenuObject.has("week3")) weekly3MenuArray =
                    monthlyMenuObject.getJSONArray("week3")

                if (monthlyMenuObject.has("week4")) weekly4MenuArray =
                    monthlyMenuObject.getJSONArray("week4")

                //   var monthlyMenu : MonthlyMenu
                for (i in 0 until weekly1MenuArray.length()) {
                    val weeklyJson = weekly1MenuArray.getJSONObject(i)
                    val itemsArray = weeklyJson?.getJSONArray("items")
                    val gson = GsonBuilder().create()
                    val theList = gson.fromJson<ArrayList<String>>(itemsArray.toString(), object :
                        TypeToken<ArrayList<String>>() {}.type)
                    val model = WeeklyMenu(
                        "",
                        weeklyJson.getInt("id"),
                        weeklyJson.getInt("dayCount"),
                        theList,
                        weeklyJson.getString("image")
                    )
                    weeklyMenuList.add(model)
                }

                monthlyMenuList.add(MonthlyMenu("Week1", weeklyMenuList))
                weeklyMenuList = ArrayList()
                for (i in 0 until weekly2MenuArray.length()) {
                    val weeklyJson = weekly2MenuArray.getJSONObject(i)
                    val itemsArray = weeklyJson?.getJSONArray("items")
                    val gson = GsonBuilder().create()
                    val theList = gson.fromJson<ArrayList<String>>(itemsArray.toString(), object :
                        TypeToken<ArrayList<String>>() {}.type)
                    val model = WeeklyMenu(
                        "",
                        weeklyJson.getInt("id"),
                        weeklyJson.getInt("dayCount"),
                        theList,
                        weeklyJson.getString("image")
                    )
                    weeklyMenuList.add(model)
                }
                monthlyMenuList.add(MonthlyMenu("Week2", weeklyMenuList))
                weeklyMenuList = ArrayList()
                for (i in 0 until weekly3MenuArray.length()) {
                    val weeklyJson = weekly3MenuArray.getJSONObject(i)
                    val itemsArray = weeklyJson?.getJSONArray("items")
                    val gson = GsonBuilder().create()
                    val theList = gson.fromJson<ArrayList<String>>(itemsArray.toString(), object :
                        TypeToken<ArrayList<String>>() {}.type)
                    val model = WeeklyMenu(
                        "",
                        weeklyJson.getInt("id"),
                        weeklyJson.getInt("dayCount"),
                        theList,
                        weeklyJson.getString("image")
                    )
                    weeklyMenuList.add(model)
                }
                monthlyMenuList.add(MonthlyMenu("Week3", weeklyMenuList))
                weeklyMenuList = ArrayList()
                for (i in 0 until weekly4MenuArray.length()) {
                    val weeklyJson = weekly4MenuArray.getJSONObject(i)
                    val itemsArray = weeklyJson?.getJSONArray("items")
                    val gson = GsonBuilder().create()
                    val theList = gson.fromJson<ArrayList<String>>(itemsArray.toString(), object :
                        TypeToken<ArrayList<String>>() {}.type)
                    val model = WeeklyMenu(
                        "",
                        weeklyJson.getInt("id"),
                        weeklyJson.getInt("dayCount"),
                        theList,
                        weeklyJson.getString("image")
                    )
                    weeklyMenuList.add(model)
                }
                monthlyMenuList.add(MonthlyMenu("Week4", weeklyMenuList))
                AppUtils.monthlyMenuList = monthlyMenuList

                weeklyMenuList = ArrayList()
            } else {
                //week menu parsing
                for (i in 0 until wklyMenuArray.length()) {
                    val dayJson = wklyMenuArray.getJSONObject(i)
                    val itemsArray = dayJson?.getJSONArray("items")
                    val gson = GsonBuilder().create()
                    val theList = gson.fromJson<ArrayList<String>>(itemsArray.toString(), object :
                        TypeToken<ArrayList<String>>() {}.type)
                    val model = WeeklyMenu(
                        "Day " + (i + 1),
                        dayJson.getInt("id"),
                        dayJson.getInt("dayCount"),
                        theList,
                        dayJson.getString("image")
                    )
                    weeklyMenuList.add(model)
                }
                AppUtils.wklyMenuList = weeklyMenuList
            }

            GlobalScope.launch(Dispatchers.Main) {
                for (i in 0 until if (planType.contains("M")) monthlyMenuList.size else weeklyMenuList.size) {
                    _binding!!.menuTab.addTab(
                        _binding!!.menuTab.newTab().setText(
                            if (planType.contains("M")) monthlyMenuList.get(i).name else weeklyMenuList[i].name
                        )
                    )
                    if(isAdded) {
                        val adapter =
                            MenuTabAdapter(
                                requireActivity(),
                                childFragmentManager,
                                planType,
                                if (planType.contains("M")) monthlyMenuList.size else weeklyMenuList.size
                            )
                        _binding!!.viewPager.setAdapter(adapter)
                    }
                }
            }
            binding.pBar.visibility = View.GONE
        }
    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        binding.pBar.visibility = View.GONE
        AppUtils.showErrorMsg(error, tag.toString(), requireActivity())
    }

}