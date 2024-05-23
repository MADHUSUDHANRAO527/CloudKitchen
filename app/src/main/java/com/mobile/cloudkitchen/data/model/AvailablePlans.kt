package com.example.example

import com.google.gson.annotations.SerializedName


data class AvailablePlans (

  @SerializedName("_id"         ) var Id          : String?             = null,
  @SerializedName("name"        ) var name        : String?             = null,
  @SerializedName("description" ) var description : String?             = null,
  @SerializedName("features"    ) var features    : ArrayList<Features> = arrayListOf(),
  @SerializedName("noOfMeals"   ) var noOfMeals   : Int?                = null,
  @SerializedName("type"        ) var type        : String?             = null

)