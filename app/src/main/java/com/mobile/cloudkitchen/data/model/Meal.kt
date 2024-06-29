package com.example.example

import com.google.gson.annotations.SerializedName


data class Meal(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("images") var images: ArrayList<String>? = null

)