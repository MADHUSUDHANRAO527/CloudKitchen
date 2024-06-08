package com.mobile.cloudkitchen.service

import android.R.attr
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.mobile.cloudkitchen.data.model.Addresses
import com.mobile.cloudkitchen.utils.UserUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object APIService {
    /* @POST("users/sendOtp")
     fun requestOtp(@Body body: Map<String, String>): Call<ResponseBody>*/
    // const val DOMAIN = "https://meal-box.onrender.com"
    //to get IP address from MAC   use this command on terminal:  ipconfig getifaddr en0
    //const val DOMAIN = "http://192.168.0.7:8000"
    //  const val DOMAIN = "https://weary-cape-colt.cyclic.app"
    // const val DOMAIN = "https://meal-box.onrender.com"
    const val DOMAIN = "https://whale-app-ct2dl.ondigitalocean.app"

    init {
        println("Singleton class invoked.")
    }

    fun makeLoginOTPRequest(
        mContext: Context,
        tag: String,
        listener: ServiceResponse?,
        phoneNumber: String
    ) {
        Log.d("URL:", DOMAIN + "/users/sendOtp")
        val json = JSONObject()
        try {
            json.put("mobileNumber", "+91 " + phoneNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, DOMAIN + "/users/sendOtp", json,
            { response ->
                listener?.onSuccessResponse(response, tag)
            }, { error ->
                listener?.onFailureResponse(error, tag)
            }
        )
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        );


        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(jsonObjectRequest)
    }

    fun makeVerifyOTPRequest(
        mContext: Context?,
        tag: String,
        listener: ServiceResponse?,
        phoneNumber: String,
        otp: String
    ) {
        Log.d("URL:", DOMAIN + "/users/verifyOtp")
        val json = JSONObject()
        try {
            json.put("otp", otp)
            json.put("mobileNumber", "+91 " + phoneNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, DOMAIN + "/users/verifyOtp", json,
            { response ->
                //{"message":"OTP verified successfully","data":{"_id":"65d23bd7d723a6b3c41353ef","mobileNumber":"+91 6300592930","isAppAdmin":false,"userType":"user","createdAt":"2024-02-18T17:18:15.379Z","updatedAt":"2024-03-15T11:15:50.189Z","__v":0,"jwtToken":{"accessToken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NWQyM2JkN2Q3MjNhNmIzYzQxMzUzZWYiLCJ1c2VyVHlwZSI6InVzZXIiLCJpYXQiOjE3MTA1MDEzOTUsImV4cCI6MTcxMDU4Nzc5NX0.oODt2rJjl2n616x2wuvJt4Njj_-DGUhjJuSENe97FFI","expiresIn":"24h","expiredAt":1710587795547}}}
                listener?.onSuccessResponse(response, tag)
            }, { error ->
                listener?.onFailureResponse(error, tag)
            }
        )
        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(jsonObjectRequest)
    }

    fun makeDashboardBannersAPICall(
        mContext: Context,
        listener: ServiceResponse?,
        tag: String
    ) {
        Log.d("URL:", DOMAIN + "/" + tag)
        val request = JsonArrayRequest(
            Request.Method.GET,
            DOMAIN + "/kitchens",
            null,
            Response.Listener { response ->
                try {
                    listener?.onSuccessResponse(tag, response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error ->
            listener?.onFailureResponse(error, tag)
        }
        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(request)
    }

    fun makeDashboardBannersAPICall(
        mContext: Context,
        listener: ServiceResponse?,
        tag: String,
        token: String?
    ) {
        Log.d("URL:", DOMAIN + "/kitchens")
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, DOMAIN + "/kitchens",
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + token.toString()
                    return headers
                }
            }
        queue.add(stringRequest)
    }

    fun makeDashboardMealsAPICall(
        mContext: Context,
        listener: ServiceResponse?,
        tag: String,
        token: String?
    ) {
        Log.d("URL:", DOMAIN + "/$tag")
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, DOMAIN + "/$tag",
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + token.toString()
                    return headers
                }
            }
        queue.add(stringRequest)
    }

    fun makeKitchenDetailsAPICall(
        mContext: Context,
        listener: ServiceResponse?,
        tag: String,
    ) {
        Log.d("URL:", DOMAIN + "/$tag")
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, DOMAIN + "/$tag",
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }
            }
        queue.add(stringRequest)
    }

    fun addEditDeleteUserAddress(
        mContext: Activity,
        tag: String,
        listener: ServiceResponse?,
        address: Addresses, isDelete: Boolean
    ) {
        Log.d("URL:", DOMAIN + "/users/${UserUtils.getUserID(mContext)}")
        val jsonAdd = JSONObject()
        val jsonAddress = JSONObject()
        var jsonAddressArray = JSONArray()
        val gson = Gson()
        if (isDelete) {
            UserUtils.getUserInfo().addresses.remove(address)
            jsonAddressArray = JSONArray(gson.toJson(UserUtils.getUserInfo().addresses))
        } else {
            if (UserUtils.getUserInfo().addresses.isNotEmpty())
                jsonAddressArray = JSONArray(gson.toJson(UserUtils.getUserInfo().addresses))
            try {
                jsonAddress.put("type", address.type)
                jsonAddress.put("fullName", address.fullName)
                jsonAddress.put("phoneNumber", address.phoneNumber)
                jsonAddress.put("houseNo", address.houseNo)
                jsonAddress.put("addressLine1", address.addressLine1)
                jsonAddress.put("addressLine2", address.addressLine2)
                jsonAddress.put("city", address.city)
                jsonAddress.put("state", address.state)
                jsonAddress.put("pincode", address.pincode)
                jsonAddress.put("country", address.country)
                jsonAddressArray.put(jsonAddress)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        jsonAdd.put("addresses", jsonAddressArray)

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.PUT,
                DOMAIN + "/users/${UserUtils.getUserID(mContext)}", jsonAdd,
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }
            }

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        );


        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(jsonObjectRequest)
    }

    fun getUserAddresses(
        mContext: Activity,
        listener: ServiceResponse?,
        tag: String,
    ) {
        Log.d("URL:", DOMAIN + "/users/${UserUtils.getUserID(mContext)}")
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, DOMAIN + "/users/${UserUtils.getUserID(mContext)}",
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }

            }

        queue.add(stringRequest)
    }

    fun processOrder1(
        mContext: Activity,
        listener: ServiceResponse?,
        tag: String,
    ) {
        Log.d("URL:", DOMAIN + "${tag}")
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, DOMAIN + "${tag}",
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }

                override fun getParams(): Map<String?, String?>? {
                    val json = HashMap<String?, String?>()
                    try {
                        json.put("planId", UserUtils.planId)
                        json.put("mealId", UserUtils.mealID)
                        json.put("startDate", UserUtils.fromDate)
                        json.put("endDate", UserUtils.toDate)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    return json
                }
            }

        queue.add(stringRequest)
    }

    fun processOrderr(
        mContext: Activity,
        tag: String,
        listener: ServiceResponse?,
    ) {
        Log.d("URL:", DOMAIN + "${tag}")
        val json = JSONObject()
        try {
            json.put("planId", UserUtils.planId)
            json.put("mealId", UserUtils.mealID)
            json.put("startDate", UserUtils.fromDate)
            json.put("endDate", UserUtils.toDate)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET,
                DOMAIN + "${tag}", json,
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }
            }

        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(jsonObjectRequest)
    }

    fun processOrder(
        mContext: Activity,
        listener: ServiceResponse?,
        tag: String,
    ) {
        val url =
            DOMAIN + tag + "?planId=${UserUtils.planId}&mealId=${UserUtils.mealID}&startDate=${UserUtils.fromDate}&endDate=${UserUtils.toDate}"
        Log.d("URL:", url)
        val queue = Volley.newRequestQueue(mContext)
        val stringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    try {
                        listener?.onSuccessResponse(response, tag)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    it.printStackTrace()
                    listener?.onFailureResponse(it, tag)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                    return headers
                }

            }

        queue.add(stringRequest)
    }

    fun callCreateOrder(
        mContext: Context,
        tag: String,
        listener: ServiceResponse?,
        data: JSONObject
    ) {
        Log.d("URL:", DOMAIN + "/orders")
     //   val json = JSONObject(data)
        val req: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, DOMAIN + "/orders",
            data,
            Response.Listener<JSONObject> { response ->
                listener?.onSuccessResponse(response, tag)
            },
            Response.ErrorListener { error ->
                listener?.onFailureResponse(error, tag)
            }) {
            /**
             * Passing some request headers
             */
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                //headers.put("Content-Type", "application/json");
                headers["Authorization"] = "Bearer " + UserUtils.getUserToken(mContext)
                return headers
            }
        }
        val requestQueue = Volley.newRequestQueue(mContext)
        requestQueue.add(req)
    }
}