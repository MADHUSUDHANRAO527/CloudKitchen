package com.mobile.cloudkitchen.utils

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.volley.AuthFailureError
import com.android.volley.ClientError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.mobile.cloudkitchen.data.model.MonthlyMenu
import com.mobile.cloudkitchen.data.model.UserAddress
import com.mobile.cloudkitchen.data.model.WeeklyMenu
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object AppUtils {
    fun fetchCurrentLocation(activity: Activity):String {

        LocationPermissionUtil.checkLocationPermissions(activity,null)

        return ""
    }

    var monthlyMenuList = ArrayList<MonthlyMenu>()
        get() = field
        set(value) {
            field = value
        }

    var wklyMenuList = ArrayList<WeeklyMenu>()
        get() = field
        set(value) {
            field = value
        }

    fun enableGPSLocation(activity: Activity ){
        var locationRequest: LocationRequest? = null
        var googleApiClient: GoogleApiClient? = null
        var result: PendingResult<LocationSettingsResult>? = null
        val REQUEST_LOCATION = 100
        googleApiClient = GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)

        result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient!!, builder.build())
        result!!.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // Do something
                    getLocation(activity)
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    try {
                        status.startResolutionForResult(
                            activity as Activity,
                            REQUEST_LOCATION
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Do something
                }
            }
        }

    }
    /*fun  permissionsGranted() : Boolean{
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }*/
    fun getLocation(activity: Activity){

        var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          //  permissionRequest.launch(locationPermissions)

        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(activity, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    val lat = location.latitude
                    val long = location.longitude

                    var geocoder = Geocoder(activity, Locale.getDefault())
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val addressForAPI33AndAbove =
                                geocoder.getFromLocation(lat, long, 1,
                                    object : Geocoder.GeocodeListener {
                                        override fun onGeocode(p0: MutableList<Address>) {
                                            val address = p0[0]
                                            val userAddress = loadUserAddress(address, lat, long)
                                            //use userAddress :)
                                        //    selectedAddress = userAddress.area!!
                                         //  val array = userAddress.fullAddressLine!!
                                            EventBus.getDefault().postSticky("Home"+"*"+userAddress.fullAddressLine!!)
                                            (activity as HomeActivity?)?.popBack()
                                        }
                                    })
                        } else {

                            val address = geocoder.getFromLocation(lat, long, 1)?.get(0)
                            if (address != null) {
                                val userAddress = loadUserAddress(address, lat, long)
                                //selectedAddress = userAddress.area!!

                                EventBus.getDefault().postSticky("Home"+"*"+userAddress.fullAddressLine!!)
                                (activity as HomeActivity?)?.popBack()
                                //use userAddress :)
                            } else {
                                //todo: Snackbar error msg
                            }
                        }
                    } catch (e: IOException) {
                        // Utils.log("Check internet: ${e.message}")
                    }


                }

            }

    }
    fun loadUserAddress(address: Address, lat: Double, lng: Double): UserAddress {
        return UserAddress(
            postcode = address.postalCode,
            fullAddressLine = address.getAddressLine(0),
            latitude = lat,
            longitude = lng,
            adminArea = address.adminArea,
            area = address.thoroughfare,
            subAdminArea = address.subAdminArea
        )
    }
    fun getTomoDate(fromDate:Boolean):String{
        val calendar: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val todayDate = df.format(calendar)
        val c = Calendar.getInstance()
        try {
            c.time = df.parse(todayDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if(UserUtils.planType.contains("M")){
            c.add(
                Calendar.DAY_OF_MONTH,
                if(fromDate) 1  else 30
            )
        }else {
            c.add(
                Calendar.DAY_OF_MONTH,
                if(fromDate) 1  else 7
            )
        }

        val output = df.format(c.time)
        return output
    }
    fun showErrorMsg(error: VolleyError, tag: String?, activity: Activity) {
        var mTag = tag
        try {
            val json = JSONObject(String(error.networkResponse.data, charset("UTF-8")))
            if (json.has("message")) {
               mTag = mTag.plus(json.getString("message")+"$mTag")
            }
        } catch (e: Exception) {
            showToast(
                activity,
                "e.message$mTag"
            )
        }
        error.networkResponse?.statusCode?.let {
            mTag = "${"-"}" + mTag.plus(error.networkResponse.statusCode)
        }

        if (error is ClientError) {
            showToast(activity, "Error:" + error.message.toString() + "/$mTag")
        } else if (error is NetworkError) {
            showToast(
                activity,
                "NetworkError!$mTag"
            )
        } else if (error is ServerError) {
            showToast(
                activity,
                "ServerError!$mTag"
            )
        } else if (error is AuthFailureError) {
            showToast(
                activity,
                "AuthFailureError!$mTag")
        } else if (error is ParseError) {
            showToast(
                activity,
                "ParseError!$mTag")
        } else if (error is NoConnectionError) {
            showToast(
                activity,
                "NoConnectionError!$mTag")
        } else if (error is TimeoutError) {
            showToast(
                activity,
                "Oops. Timeout error!$mTag")
        } else if (error is ClientError) {
            showToast(
                activity,
                "Oops. ClientError!$mTag")
        } else if(error is VolleyError){
            showToast(
                activity,
                "Oops. VolleyError!$mTag")
        }else{
            showToast(
                activity,
                "${error.message}$mTag")
        }
    }
     fun showToast(activity: Activity, msg:String){
        Toast.makeText(
            activity,
            msg,
            Toast.LENGTH_LONG
        ).show();
    }
}