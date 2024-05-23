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
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.mobile.cloudkitchen.ui.adapter.KitchensAdapter
import com.mobile.cloudkitchen.databinding.FragmentHomeBinding
import com.mobile.cloudkitchen.data.model.DashBoardBannerModel
import com.mobile.cloudkitchen.data.model.KitchensModel
import com.mobile.cloudkitchen.data.viewmodels.HomeVM
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.ui.adapter.HomeMainSliderAdapter
import com.mobile.cloudkitchen.utils.AppUtils
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class HomeFragment : Fragment(), ServiceResponse {

    private var _binding: FragmentHomeBinding? = null
    private var bannersList = ArrayList<DashBoardBannerModel>()
    private var kitchensModelList = ArrayList<KitchensModel>()
    lateinit var sp: SharedPreferences
    private lateinit var mealsAdapter: KitchensAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeVM::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.rvMeals.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        APIService.makeDashboardMealsAPICall(requireActivity(), this, "meals",sp.getString("TOKEN","NA"))
        APIService.makeDashboardBannersAPICall(requireActivity(), this, "kitchens",sp.getString("TOKEN","NA"))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccessResponse (response: Any?,tag: Any?) {
        if (tag.toString().contentEquals("meals")) {
            var jsonArray = JSONArray(response as String)
            for (i in 0 until jsonArray.length()) {
                val bannerJson = jsonArray.getJSONObject(i)
                // model.bannerid = bannerJson.getInt("Id")
                //  model.bannerImg = bannerJson.getString("URL")

                val model = DashBoardBannerModel(
                    null,
                    null,
                    null,
                    null,
                    bannerJson.getJSONArray("images"),
                    null,bannerJson.getString("bannerImage")
                )

                // model.bannerName = bannerJson.getString("Name")
                bannersList.add(model)
            }
            GlobalScope.launch(Dispatchers.Main) {
                setImageInSlider(bannersList, binding.imageSlider)
            }
            binding.homepBar.visibility = View.GONE
        }else if (tag.toString().contentEquals("kitchens")) {
            binding.kitchenTitleTxt.visibility = View.VISIBLE
            var jsonArray = JSONArray(response as String)
            for (i in 0 until jsonArray.length()) {
                val bannerJson = jsonArray.getJSONObject(i)
                // model.bannerid = bannerJson.getInt("Id")
                //  model.bannerImg = bannerJson.getString("URL")
                if (bannerJson.has("bannerImage")) {
                    val model = KitchensModel(bannerJson.getString("_id"),
                        bannerJson.getString("name"),
                        bannerJson.getString("description"),
                        "",
                        "",
                        if (bannerJson.get("avgRating").toString().contentEquals("null")) 0.0 else bannerJson.getDouble("avgRating"),
                        if (bannerJson.get("noOfRatings").toString().contentEquals("null")) 0.0 else bannerJson.getDouble("noOfRatings"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        bannerJson.getJSONArray("type").toString() ,
                        if(bannerJson.has("startingPrice")) bannerJson.getInt("startingPrice") else 0,
                        bannerJson.getString("bannerImage"),
                    )
                    kitchensModelList.add(model)
                }
                // model.bannerName = bannerJson.getString("Name")
            }
            GlobalScope.launch(Dispatchers.Main) {
                mealsAdapter = KitchensAdapter(requireActivity(),kitchensModelList)
                binding.rvMeals.adapter = mealsAdapter
            }
            binding.homepBar.visibility = View.GONE
        }
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        binding.homepBar.visibility = View.GONE
        AppUtils.showErrorMsg(error, tag.toString(), requireActivity())
    }

    private fun setImageInSlider(images: ArrayList<DashBoardBannerModel>, imageSlider: SliderView) {
        val adapter = HomeMainSliderAdapter(requireActivity())
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(true, true)
    }
}