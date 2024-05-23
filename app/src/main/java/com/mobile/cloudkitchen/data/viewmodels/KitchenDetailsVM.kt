package com.mobile.cloudkitchen.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.example.Kitchen
import com.mobile.cloudkitchen.data.repos.MainRepository

class KitchenDetailsVM : ViewModel() {
    private val kitchenDetailsData = MutableLiveData<Kitchen>()
    private val repository = MainRepository()

    fun callKitchenDetails(){
        val details = repository.makeKitchenDetailsAPICall()
    }
}