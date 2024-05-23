package com.mobile.cloudkitchen.data.model

import com.example.example.Location
import com.google.gson.annotations.SerializedName


data class UserInfo(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("isAppAdmin") var isAppAdmin: Boolean? = null,
    @SerializedName("ownedKitchens") var ownedKitchens: ArrayList<String> = arrayListOf(),
    @SerializedName("addresses") var addresses: ArrayList<Addresses> = arrayListOf(),
    @SerializedName("image") var image: String? = null,
    @SerializedName("location") var location: Location? = Location(),
    @SerializedName("userType") var userType: String? = null

)