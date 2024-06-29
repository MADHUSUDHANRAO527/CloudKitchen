package com.example.example

import com.google.gson.annotations.SerializedName
import com.mobile.cloudkitchen.data.model.DeliveryAddress
import com.mobile.cloudkitchen.data.model.Plan
import com.mobile.cloudkitchen.data.model.User


data class Subscriptions(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("meal") var meal: Meal? = Meal(),
    @SerializedName("kitchen") var kitchen: SubscriptionKitchen? = SubscriptionKitchen(),
    @SerializedName("user") var user: User? = User(),
    @SerializedName("plan") var plan: Plan? = Plan(),
    @SerializedName("delieveryAddress") var delieveryAddress: DeliveryAddress? = DeliveryAddress(),
    @SerializedName("amount") var amount: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("plannedDates") var plannedDates: ArrayList<String> = arrayListOf(),
    @SerializedName("pausedDates") var pausedDates: ArrayList<String> = arrayListOf(),
    @SerializedName("donatedDates") var donatedDates: ArrayList<String> = arrayListOf(),
    @SerializedName("deliveredDates") var deliveredDates: ArrayList<String> = arrayListOf(),
    @SerializedName("undeliveredDates") var undeliveredDates: ArrayList<String> = arrayListOf(),
    @SerializedName("startDate") var startDate: String? = null,
    @SerializedName("endDate") var endDate: String? = null,
    @SerializedName("deliveryTimeSlot") var deliveryTimeSlot: String? = null,
    @SerializedName("createdBy") var createdBy: String? = null,
    @SerializedName("order") var order: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null

)