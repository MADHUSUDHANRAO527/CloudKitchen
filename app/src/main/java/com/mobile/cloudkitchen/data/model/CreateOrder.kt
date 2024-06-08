package com.example.example

import com.google.gson.annotations.SerializedName
import com.mobile.cloudkitchen.data.model.Addresses


data class CreateOrder(

    @SerializedName("user") var user: String? = null,
    @SerializedName("kitchen") var kitchen: String? = null,
    @SerializedName("meal") var meal: String? = null,
    @SerializedName("plan") var plan: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("deliveryInstructions") var deliveryInstructions: String? = null,
    @SerializedName("couponCode") var couponCode: String? = null,
    @SerializedName("paymentType") var paymentType: String? = null,
    @SerializedName("savedAmount") var savedAmount: Double? = null,
    @SerializedName("totalAmount") var totalAmount: Double? = null,
    @SerializedName("deliveryCharges") var deliveryCharges: Double? = null,
    @SerializedName("grandTotal") var grandTotal: Double? = null,
    @SerializedName("deliveryAddress") var deliveryAddress: Addresses? = Addresses(),
    @SerializedName("isPaymentDone") var isPaymentDone: Boolean? = null,
    @SerializedName("planStartDate") var planStartDate: String? = null,
    @SerializedName("planEndDate") var planEndDate: String? = null,
    @SerializedName("deliveryTimeSlot") var deliveryTimeSlot: String? = null

)