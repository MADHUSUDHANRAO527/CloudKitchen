package com.example.example

import com.google.gson.annotations.SerializedName


data class Kitchen(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("addresses") var addresses: ArrayList<String> = arrayListOf(),
    @SerializedName("availablePlans") var availablePlans: ArrayList<AvailablePlans> = arrayListOf(),
    @SerializedName("badges") var badges: ArrayList<String> = arrayListOf(),
    @SerializedName("closingHours") var closingHours: String? = null,
    @SerializedName("contact") var contact: ArrayList<String> = arrayListOf(),
    @SerializedName("description") var description: String? = null,
    @SerializedName("discounts") var discounts: ArrayList<String> = arrayListOf(),
    @SerializedName("images") var images: ArrayList<String> = arrayListOf(),
    @SerializedName("location") var location: Location? = Location(),
    @SerializedName("meals") var meals: ArrayList<Meals> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("openingHours") var openingHours: String? = null,
    @SerializedName("paymentsAccepted") var paymentsAccepted: ArrayList<String> = arrayListOf(),
    @SerializedName("avgRating") var avgRating: Double? = null,
    @SerializedName("reviews") var reviews: ArrayList<Reviews> = arrayListOf(),
    @SerializedName("startingPrice") var startingPrice: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("thumbUrl") var thumbUrl: String? = null,
    @SerializedName("type") var type: ArrayList<String> = arrayListOf(),
    @SerializedName("noOfRatings") var noOfRatings: Int? = null,
    @SerializedName("bannerImage") var bannerImage: String? = null,
    @SerializedName("searchTags") var searchTags: ArrayList<String> = arrayListOf(),
    @SerializedName("owner") var owner: Owner? = Owner()

)