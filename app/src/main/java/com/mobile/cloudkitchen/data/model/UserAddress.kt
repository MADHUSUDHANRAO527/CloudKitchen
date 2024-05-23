package com.mobile.cloudkitchen.data.model

data class UserAddress(
    var postcode: String?,
    var fullAddressLine: String?,
    var latitude: Double?,
    var longitude: Double?,
    var adminArea: String?,
    var area: String?,
    var subAdminArea: String?
)
