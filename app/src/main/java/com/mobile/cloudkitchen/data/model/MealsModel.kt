package com.mobile.cloudkitchen.data.model

data class MealsModel(var _id:String,var title:String,var description:String,var addresses:String,var availablePlans:String,
                      var avgRating:Double,var noOfRatings:Double,var reviews:String,var images:String,
                      var location:String,var badges:String,var meals:String,var startingPrice:Int,var bannerImage: String,var wklySubscriptionPrice:Int,
                        var monthlySubscriptionPrice:Int
                )
data class MonthlyMenu(var name:String,var wklyMenu:ArrayList<WeeklyMenu>)
data class WeeklyMenu(var name:String,var _id:Int,var dayCount:Int,var menuItems : ArrayList<String>,var images: String)

