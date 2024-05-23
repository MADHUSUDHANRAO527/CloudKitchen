package com.example.example

import com.google.gson.annotations.SerializedName


data class Features (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("description" ) var description : String? = null

)