package com.example.example

import com.google.gson.annotations.SerializedName


data class SubscriptionKitchen(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("bannerImage") var bannerImage: String? = null

)