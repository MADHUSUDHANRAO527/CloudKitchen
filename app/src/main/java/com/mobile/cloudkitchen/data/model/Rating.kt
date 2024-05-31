package com.example.example

import com.google.gson.annotations.SerializedName


data class Rating(

    @SerializedName("avgRating") var avgRating: Double? = null,
    @SerializedName("ratings") var ratings: ArrayList<Double> = arrayListOf(),
    @SerializedName("noOfRatings") var noOfRatings: Int? = null

)