package com.mobile.cloudkitchen.utils

import android.app.Activity
import android.content.Context
import com.example.example.Kitchen
import com.mobile.cloudkitchen.data.model.Addresses
import com.mobile.cloudkitchen.data.model.UserInfo

object UserUtils {
    private lateinit var userInfo : UserInfo
    private lateinit var addresses: Addresses
    private lateinit var kitchen: Kitchen

    fun getUserID(activity: Activity): String? {
         return activity.getSharedPreferences("SP", Context.MODE_PRIVATE).getString("USERID","NA")
    }
    fun getUserToken(activity: Activity):String?{
        return activity.getSharedPreferences("SP", Context.MODE_PRIVATE).getString("TOKEN","NA")
    }
    fun setUserInfo(mUserInfo : UserInfo){
        this.userInfo = mUserInfo
    }
    fun getUserInfo() : UserInfo {
        return userInfo
    }
    fun setKitchen(mKitchen:Kitchen){
        this.kitchen = mKitchen
    }
    fun getKitchen(): Kitchen{
        return kitchen
    }
    fun setSelectedAddress(mAddresses: Addresses){
        this.addresses = mAddresses
    }
    fun getSelectedAddress(): Addresses {
        return addresses
    }
    var planType : String =""
        get() = field
        set(value) {
            field = value
        }
    var planId : String =""
        get() = field
        set(value) {
            field = value
        }
    var fromDate : String =""
        get() = field
        set(value) {
            field = value
        }
    var toDate : String =""
        get() = field
        set(value) {
            field = value
        }
    var timeSlot : String =""
        get() = field
        set(value) {
            field = value
        }

}