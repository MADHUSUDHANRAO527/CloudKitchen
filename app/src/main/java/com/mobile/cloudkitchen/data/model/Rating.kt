package com.example.example

import com.google.gson.annotations.SerializedName


data class Rating(

    @SerializedName("avgRating") var avgRating: Int? = null,
    @SerializedName("ratings") var ratings: ArrayList<Int> = arrayListOf(),
    @SerializedName("noOfRatings") var noOfRatings: Int? = null

)