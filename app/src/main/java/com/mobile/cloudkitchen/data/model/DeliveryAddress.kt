package com.mobile.cloudkitchen.data.model

import com.google.gson.annotations.SerializedName


data class DeliveryAddress (

  @SerializedName("houseNo"      ) var houseNo      : String? = null,
  @SerializedName("addressLine1" ) var addressLine1 : String? = null,
  @SerializedName("addressLine2" ) var addressLine2 : String? = null,
  @SerializedName("city"         ) var city         : String? = null,
  @SerializedName("state"        ) var state        : String? = null,
  @SerializedName("pincode"      ) var pincode      : String? = null,
  @SerializedName("country"      ) var country      : String? = null

)