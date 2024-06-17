package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.mobile.cloudkitchen.data.model.KitchensModel
import com.mobile.cloudkitchen.data.model.Orders
import com.mobile.cloudkitchen.databinding.FragmentContactUsBinding
import com.mobile.cloudkitchen.databinding.FragmentOrdersHistoryBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.adapter.KitchensAdapter
import com.mobile.cloudkitchen.ui.adapter.OrderHistoryAdapter
import com.mobile.cloudkitchen.utils.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class OrdersHistoryFragment : Fragment(), ServiceResponse {

    private var _binding: FragmentOrdersHistoryBinding? = null
    private val binding get() = _binding!!
    private var number: String = " "
    private var mail: String = " "
    lateinit var sp: SharedPreferences
    private lateinit var ordersHistoryAdapter: OrderHistoryAdapter
    private var kitchensModelList = ArrayList<Orders>()

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
        val ordersList = gson.fromJson(response.toString(), Orders::class.java)

        kitchensModelList.add(ordersList)

        GlobalScope.launch(Dispatchers.Main) {
            ordersHistoryAdapter = OrderHistoryAdapter(requireActivity(), kitchensModelList)
            binding.rvOrders.adapter = ordersHistoryAdapter
        }
        _binding?.ordersPbar?.visibility = View.GONE

    }


    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        TODO("Not yet implemented")
    }
}