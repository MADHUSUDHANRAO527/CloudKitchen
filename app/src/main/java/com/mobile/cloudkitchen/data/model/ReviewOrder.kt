package com.mobile.cloudkitchen.data.model

import com.google.gson.annotations.SerializedName

data class ReviewOrder(
    @SerializedName("discount") var discount: String? = null,
    @SerializedName("grandTotal") var grandTotal: Int? = 0,
    @SerializedName("plannedDates") var plannedDates: ArrayList<String> = arrayListOf(),
    @SerializedName("savedAmount") var savedAmount: Int? = 0,
    @SerializedName("totalAmount") var totalAmount: Int? = 0
)