package com.example.example

import com.google.gson.annotations.SerializedName


data class Plan (

  @SerializedName("_id"  ) var Id   : String? = null,
  @SerializedName("name" ) var name : String? = null

)