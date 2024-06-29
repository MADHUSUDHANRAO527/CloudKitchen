package com.mobile.cloudkitchen.data.model

import com.example.example.Meal
import com.google.gson.annotations.SerializedName
data class Orders(
    @SerializedName("user") var user: User? = User(),
    @SerializedName("meal") var meal: Meal? = Meal(),
    @SerializedName("plan") var plan: Plan? = Plan(),
    @SerializedName("createdBy") var createdBy: String? = null,
    @SerializedName("deliveryAddress") var deliveryAddress: Addresses? = null,
    @SerializedName("grandTotal") var grandTotal: Int? = null,
    @SerializedName("isPaymentDone") var isPaymentDone: Boolean? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null
)