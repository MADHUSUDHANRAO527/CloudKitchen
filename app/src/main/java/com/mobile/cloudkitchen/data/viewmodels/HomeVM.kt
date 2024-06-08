package com.mobile.cloudkitchen.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.cloudkitchen.data.model.DashBoardBannerModel
import com.mobile.cloudkitchen.data.repos.MainRepository

class HomeVM : ViewModel() {
    private val dashboardBannerLiveData = MutableLiveData<DashBoardBannerModel>()
    val weatherData: LiveData<DashBoardBannerModel> = dashboardBannerLiveData
    private val repository = MainRepository()
  //  private val context = getApplication<Application>().applicationContext
    fun makeDashboardMealsAPICall(){
        //repository.makeKitchenDetailsAPICall()
    }

}