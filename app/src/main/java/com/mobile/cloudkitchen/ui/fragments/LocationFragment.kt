package com.mobile.cloudkitchen.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.mobile.cloudkitchen.data.model.UserInfo
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
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


class LocationFragment : Fragment(),ServiceResponse, AddressAdapter.AdapterCallback {

    private var _binding: FragmentLocationBinding? = null
    private lateinit var addressAdapter: AddressAdapter
    private var addressList = ArrayList<UserAddress>()
    protected val REQUEST_CHECK_SETTINGS = 0x1
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var selectedAddress : String

    // This property is only valid between onCreateView and
    // onDestroyView.
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
       // binding.rvAddresses.layoutManager = GridLayoutManager(activity, 2)
      //  prepareAddressList()
        refreshAddress()

        val textView: TextView = binding.textNotifications
        binding.icBack.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }
        /*notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

      //  }
        /* if (checkPermissions()) {
             // code if permission granted already
             // else request permission
             *//*googleApiClient = getAPIClientInstance();
            if (googleApiClient != null) {
                googleApiClient!!.connect();
            }*//*
           // requestGPSSettings()
          *//*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getLocation()
            }*//*
        } else {
            requestPermissions()
        }*/
        binding.useMyLocationBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                AppUtils.enableGPSLocation(requireActivity())
            }
           /* if (checkPermissions()) {
                // code if permission granted already
                // else request permission
              *//*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getLocation()
                }*//*
              //  requestGPSSettings()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getLocation()
                }
            } else {
                requestPermissions()
            }*/
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
    private fun getAPIClientInstance(): GoogleApiClient? {
        return GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API).build()
    }


    private fun prepareAddressList() {
        addressList.add(UserAddress(null,"Mallampet Rd, Mallampet, Hyderabad, Telangana 502325",null,null,null,null,null))
        addressList.add(UserAddress(null,"Mallampet Rd, Mallampet, Hyderabad, Telangana 502325",null,null,null,null,null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // If we want background location on Android 10.0 and higher, use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // request for permissions
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionRequest.launch(locationPermissions)
        }
    }

    // Permission result
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        permissions.entries.forEach {
            Log.e("LOG_TAG", "${it.key} = ${it.value}")
        }

        if (granted) {
            //getLocation()
            // your code if permission granted
        } else {
            // your code if permission denied
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.showHideBottomNavigation(true,true)
    }
    fun refreshAddress() {
        APIService.getUserAddresses(requireActivity(),this,"USER_ADDRESSES")
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        val gson = Gson()
        val user: UserInfo = gson.fromJson(
            response.toString(),
            UserInfo::class.java
        )
        UserUtils.setUserInfo(user)
           // GlobalScope.launch(Dispatchers.Main) {
            addressAdapter = AddressAdapter(requireActivity(),user.addresses,this)
            binding.rvAddresses.adapter = addressAdapter
     //   }
        Toast.makeText(requireActivity(), response.toString(), Toast.LENGTH_LONG)
            .show()
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        AppUtils.showErrorMsg(error, tag .toString(), requireActivity())
    }

    override fun onItemClicked(position: Int) {
        Toast.makeText(requireActivity(), UserUtils.getUserInfo().addresses.get(position).houseNo, Toast.LENGTH_LONG)
            .show()
        val bottomSheet = AddAddressDialogFragment(this,UserUtils.getUserInfo().addresses.get(position))
        bottomSheet.show(
            childFragmentManager,
            "ModalBottomSheet"
        )
    }
}
