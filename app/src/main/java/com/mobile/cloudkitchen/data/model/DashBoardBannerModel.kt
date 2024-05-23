package com.mobile.cloudkitchen.data.model

import org.json.JSONArray

data class DashBoardBannerModel(var address: String?, var availablePlans : String?, var avgRating : Float?,
                                var noOfRatings: Int?, var images: JSONArray?, var locationModel: LocationModel?, var bannerImage:String?) {
}