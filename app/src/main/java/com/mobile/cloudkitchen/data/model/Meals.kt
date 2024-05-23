package com.example.example

import com.google.gson.annotations.SerializedName


data class Meals(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("badges") var badges: ArrayList<String> = arrayListOf(),
    @SerializedName("images") var images: ArrayList<String> = arrayListOf(),
    @SerializedName("price") var price: Int? = null,
    @SerializedName("weeklySubscriptionCost") var weeklySubscriptionCost: Int? = null,
    @SerializedName("monthlySubscriptionCost") var monthlySubscriptionCost: Int? = null,
    @SerializedName("rating") var rating: Rating? = Rating()

)