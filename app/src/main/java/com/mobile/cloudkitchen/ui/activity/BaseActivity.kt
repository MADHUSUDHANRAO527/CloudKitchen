package com.mobile.cloudkitchen.ui.activity;

import android.Manifest
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle;
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

import com.mobile.cloudkitchen.R;
import com.mobile.cloudkitchen.utils.AppUtils
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.util.Locale

open class BaseActivity: AppCompatActivity() {
    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var isFromHome : Boolean = true
        get() = field
        set(value) {
            field = value
        }
    private var locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base);
        sp = this.getSharedPreferences("SP", Context.MODE_PRIVATE)
        editor = sp.edit()
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation()
        } else{
            AppUtils.enableGPSLocation(this)
        }
    }
    fun getLocation() {

        var fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequest.launch(locationPermissions)

        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    val lat = location.latitude
                    val long = location.longitude

                    var geocoder = Geocoder(this, Locale.getDefault())
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val addressForAPI33AndAbove =
                                geocoder.getFromLocation(lat, long, 1,
                                    object : Geocoder.GeocodeListener {
                                        override fun onGeocode(p0: MutableList<Address>) {
                                            val address = p0[0]
                                            val userAddress =
                                                AppUtils.loadUserAddress(address, lat, long)
                                            //use userAddress :)
                                            //    selectedAddress = userAddress.area!!
                                            //    val array = userAddress.fullAddressLine!!.split(",")
                                           if (!isFromHome)
                                                popBack()
                                          /*  binding.appBarHome.addressTxt.text =
                                                userAddress.fullAddressLine!!*/
                                            saveAddress(userAddress.fullAddressLine!!)
                                        }
                                    })
                        } else {

                            val address = geocoder.getFromLocation(lat, long, 1)?.get(0)
                            if (address != null) {
                                val userAddress = AppUtils.loadUserAddress(address, lat, long)
                                //selectedAddress = userAddress.area!!
                                EventBus.getDefault().postSticky("Home"+"*"+userAddress.fullAddressLine!!)
                                if (!isFromHome)
                                    popBack()
                                saveAddress(userAddress.fullAddressLine!!)
                                //use userAddress :)
                            } else {
                                //todo: Snackbar error msg
                            }
                        }
                    } catch (e: IOException) {
                        AppUtils.showToast(this, e.message.toString())
                    }
                }
            }
    }

    private fun saveAddress(fullAddressLine: String) {
        editor.putString(
            "SELECTED_ADDRESS",
            fullAddressLine
        )
        editor.commit()
        editor.apply()
    }

    private fun getSelectedAddress(): String? {
        return sp.getString("SELECTED_ADDRESS", "Click here to add address!")
    }
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }

            if (granted) {
                getLocation()
                // your code if permission granted
            } else {
                AppUtils.showToast(this, "You denied permission!")
                // your code if permission denied
            }
        }
    fun popBack() {
        supportFragmentManager?.popBackStack()
    }
}