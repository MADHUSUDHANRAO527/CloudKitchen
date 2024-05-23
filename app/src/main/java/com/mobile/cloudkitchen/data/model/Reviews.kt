package com.example.example

import com.google.gson.annotations.SerializedName


data class Reviews (

  @SerializedName("_id"       ) var Id        : String? = null,
  @SerializedName("user"      ) var user      : User?   = User(),
  @SerializedName("comment"   ) var comment   : String? = null,
  @SerializedName("kitchenId" ) var kitchenId : String? = null,
  @SerializedName("feedback"  ) var feedback  : String? = null,
  @SerializedName("rating"    ) var rating    : Int?    = null,
  @SerializedName("createdAt" ) var createdAt : String? = null,
  @SerializedName("updatedAt" ) var updatedAt : String? = null,
  @SerializedName("__v"       ) var _v        : Int?    = null

)