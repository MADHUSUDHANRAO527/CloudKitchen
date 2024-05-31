package com.mobile.cloudkitchen.utils

import android.app.Activity
import android.content.Context
import com.example.example.Kitchen
import com.example.example.Meals
import com.mobile.cloudkitchen.data.model.Addresses
import com.mobile.cloudkitchen.data.model.ReviewOrder
import com.mobile.cloudkitchen.data.model.UserInfo

object UserUtils {
    private var userInfo: UserInfo = UserInfo()
    private var addresses: Addresses = Addresses()
    private var kitchen: Kitchen = Kitchen()
    private var meal: Meals = Meals()
    private var processOrder: ReviewOrder = ReviewOrder()
    fun getUserID(activity: Activity): String? {
        return activity.getSharedPreferences("SP", Context.MODE_PRIVATE).getString("USERID", "NA")
    }

    fun getUserToken(activity: Context): String? {
        return activity.getSharedPreferences("SP", Context.MODE_PRIVATE).getString("TOKEN", "NA")
    }

    fun setUserInfo(mUserInfo: UserInfo) {
        this.userInfo = mUserInfo
    }

    fun getUserInfo(): UserInfo {
        return userInfo
    }

    fun setKitchen(mKitchen: Kitchen) {
        this.kitchen = mKitchen
    }

    fun getKitchen(): Kitchen {
        return kitchen
    }

    fun setMeal(meal: Meals) {
        this.meal = meal
    }

    fun getMeal(): Meals {
        return meal
    }

    var mealID: String = ""
        get() = field
        set(value) {
            field = value
        }

    fun setSelectedAddress(mAddresses: Addresses) {
        this.addresses = mAddresses
    }

    fun getSelectedAddress(): Addresses {
        return addresses
    }

    var planType: String = ""
        get() = field
        set(value) {
            field = value
        }
    var planId: String = ""
        get() = field
        set(value) {
            field = value
        }
    var fromDate: String = ""
        get() = field
        set(value) {
            field = value
        }
    var toDate: String = ""
        get() = field
        set(value) {
            field = value
        }
    var timeSlot: String = ""
        get() = field
        set(value) {
            field = value
        }
    var fromHumanDate: String = ""
        get() = field
        set(value) {
            field = value
        }
    var toHumanDate: String = ""
        get() = field
        set(value) {
            field = value
        }
    var monthlyAmount: String = ""
        get() = field
        set(value) {
            field = value
        }
    var wklyAmount: String = ""
        get() = field
        set(value) {
            field = value
        }
    fun setReviewOrder(reviewOrder: ReviewOrder) {
        this.processOrder = reviewOrder
    }

    fun getReviewOrder(): ReviewOrder {
        return processOrder
    }

}