package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.example.example.Kitchen
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mobile.cloudkitchen.data.model.Orders
import com.mobile.cloudkitchen.databinding.FragmentOrdersHistoryBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.ui.adapter.OrderHistoryAdapter
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.Arrays


class OrdersHistoryFragment : Fragment(), ServiceResponse {

    private var _binding: FragmentOrdersHistoryBinding? = null
    private val binding get() = _binding!!
    private var number: String = " "
    private var mail: String = " "
    lateinit var sp: SharedPreferences
    private lateinit var ordersHistoryAdapter: OrderHistoryAdapter
    private var ordersList = ArrayList<Orders>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.rvOrders.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        //https://whale-app-ct2dl.ondigitalocean.app/orders/user/65d23bd7d723a6b3c41353ef
        APIService.getOrders(
            requireActivity(),
            this,
            "orders/user/${UserUtils.getUserID(requireActivity())}",
        )
        return root
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        val gson = Gson()
        /*   val collectionType: Type = object : TypeToken<List<Orders?>?>() {}.type
           val lcs: List<Orders> = Gson()
               .fromJson(response.toString(), collectionType) as List<Orders>
           val ordersList = gson.fromJson(response.toString(), List<Orders>::class.java)
   */

        //   val ordersList: List<Orders> = gson.fromJson(response.toString(), List<Orders>::class.java)
        //   kitchensModelList.add(ordersList)
        var jsonArray = JSONArray(response as String)
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            try {
                val order = gson.fromJson(
                    json.toString(),
                    Orders::class.java
                )
                ordersList.add(order)
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
            ordersHistoryAdapter = OrderHistoryAdapter(requireActivity(), ordersList)
            binding.rvOrders.adapter = ordersHistoryAdapter
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