package com.example.example

import com.google.gson.annotations.SerializedName


data class Location (

  @SerializedName("houseNo"      ) var houseNo      : String? = null,
  @SerializedName("addressLine1" ) var addressLine1 : String? = null,
  @SerializedName("addressLine2" ) var addressLine2 : String? = null,
  @SerializedName("city"         ) var city         : String? = null,
  @SerializedName("state"        ) var state        : String? = null,
  @SerializedName("pincode"      ) var pincode      : String? = null,
  @SerializedName("country"      ) var country      : String? = null

)