package com.mobile.cloudkitchen.ui.fragments

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.mobile.cloudkitchen.data.model.UserInfo
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.mobile.cloudkitchen.ui.adapter.AddressAdapter
import com.mobile.cloudkitchen.databinding.FragmentLocationBinding
import com.mobile.cloudkitchen.data.model.UserAddress
import com.mobile.cloudkitchen.data.viewmodels.LocationVM
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import org.greenrobot.eventbus.EventBus


class LocationFragment : Fragment(),ServiceResponse, AddressAdapter.AdapterCallback {

    private var _binding: FragmentLocationBinding? = null
    private lateinit var addressAdapter: AddressAdapter
    private var addressList = ArrayList<UserAddress>()
    protected val REQUEST_CHECK_SETTINGS = 0x1
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var selectedAddress : String
    private val binding get() = _binding!!
    private var locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(LocationVM::class.java)

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvAddresses.layoutManager =  LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        refreshAddress()
        val textView: TextView = binding.textNotifications
        binding.icBack.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }
        binding.addAddressImg.setOnClickListener {
            val bottomSheet = AddAddressDialogFragment(
                this,
                null
            )
            bottomSheet.show(
                childFragmentManager,
                "ModalBottomSheet"
            )
        }

        binding.useMyLocationBtn.setOnClickListener {
            (activity as HomeActivity?)?.isFromHome = false
            binding.pBar.visibility = View.VISIBLE
            val manager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                (activity as HomeActivity?)?.getLocation()
            } else {
                AppUtils.enableGPSLocation(requireActivity())
            }
        }
        binding.addAddress.setOnClickListener {
            val bottomSheet = AddAddressDialogFragment(
                this,
                null
            )
            bottomSheet.show(
                childFragmentManager,
                "ModalBottomSheet"
            )
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }

    override fun onStop() {
        super.onStop()
        if (AppUtils.isFromHome)
            (activity as HomeActivity?)?.showHideBottomNavigation(true, true)
    }
    fun refreshAddress() {
        binding.lpBar.visibility = View.VISIBLE
        APIService.getUserAddresses(requireActivity(), this, "GET_ADDRESSES")
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        if(tag.toString().contains("DELETE_ADDRESS")){
            refreshAddress()
        }else {
            val gson = Gson()
            val user: UserInfo = gson.fromJson(
                response.toString(),
                UserInfo::class.java
            )
            UserUtils.setUserInfo(user)
            addressAdapter = AddressAdapter(requireActivity(), user.addresses, this)
            binding.rvAddresses.adapter = addressAdapter
            if (user.addresses.isEmpty()) {
                binding.lpBar.visibility = View.GONE
                binding.addAddressImg.isClickable = true
                binding.noAddressLayout.visibility = View.VISIBLE
            } else {
                binding.lpBar.visibility = View.GONE
                binding.noAddressLayout.visibility = View.GONE
            }
        }
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        AppUtils.showErrorMsg(error, tag .toString(), requireActivity())
    }

    override fun onItemClicked(position: Int,isDelete : Boolean) {
        if (isDelete) {
            APIService.addEditDeleteUserAddress(requireActivity(),"DELETE_ADDRESS",this,UserUtils.getUserInfo().addresses.get(position),true)
        } else {
            val bottomSheet =
                AddAddressDialogFragment(this, UserUtils.getUserInfo().addresses.get(position))
            bottomSheet.show(
                childFragmentManager,
                "ModalBottomSheet"
            )
        }
    }

    override fun onItemClick(type:String,address: String) {
        EventBus.getDefault().postSticky(type+"*"+address)
        (activity as HomeActivity?)?.popBack()
    }
}
