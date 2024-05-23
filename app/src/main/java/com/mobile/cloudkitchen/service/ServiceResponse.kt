package com.mobile.cloudkitchen.service

import com.android.volley.VolleyError

interface ServiceResponse {

    fun onSuccessResponse(response:Any?,tag:Any?)

    fun onFailureResponse(error: VolleyError,tag: Any?)

}
