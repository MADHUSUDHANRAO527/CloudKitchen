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
import com.example.example.Subscriptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.mobile.cloudkitchen.data.model.Orders
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentSubscriptionsBinding
import com.mobile.cloudkitchen.databinding.ProfileFragmentBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.ui.adapter.OrderHistoryAdapter
import com.mobile.cloudkitchen.ui.adapter.SubscriptionsAdapter
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class SubscriptionsFragment : Fragment(), ServiceResponse {
    private var _binding: FragmentSubscriptionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var planType: String
    private var subscriptionsList = ArrayList<Subscriptions>()
    private lateinit var subscriptionsAdapter: SubscriptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.rvSubscriptions.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        //https://whale-app-ct2dl.ondigitalocean.app/orders/user/65d23bd7d723a6b3c41353ef
        APIService.getSubscriptions(
            requireActivity(),
            this,
            "subscriptions/user/${UserUtils.getUserID(requireActivity())}",
        )
       // binding.nameTxt.text = sp.getString("NAME","NA")
       /* binding.orderHistoryLayout.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(OrdersHistoryFragment(), null)
        }*/
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }
    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.showHideBottomNavigation(true,true)
    }
    override fun onSuccessResponse(response: Any?, tag: Any?) {
        val gson = Gson()



        var jsonArray = JSONArray(response as String)
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            try {
                val subscriptions = gson.fromJson(
                    json.toString(),
                    Subscriptions::class.java
                )
                subscriptionsList.add(subscriptions)
            } catch (e: JsonSyntaxException) {
                AppUtils.showToast(
                    requireActivity(),
                    e.message.toString() + "-response from server side!"
                )
                continue
                // binding.viewPlanLayout.visibility = View.GONE
                //  return
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            subscriptionsAdapter = SubscriptionsAdapter(requireActivity(), subscriptionsList)
            binding.rvSubscriptions.adapter = subscriptionsAdapter
        }
        /*try {
            val ordersList = gson.fromJson(response.toString(), Array<Orders>::class.java).toList()
            GlobalScope.launch(Dispatchers.Main) {
                ordersHistoryAdapter = OrderHistoryAdapter(requireActivity(), ordersList)
                binding.rvOrders.adapter = ordersHistoryAdapter
            }
        } catch (e: JsonSyntaxException) {
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG)
                .show()
        }*/

        _binding?.backIcon?.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }

        _binding?.ordersPbar?.visibility = View.GONE

    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        TODO("Not yet implemented")
    }
}