package com.sehatq.testapplication.feature.purchase.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseViewModel

class PurchaseViewModel : BaseViewModel() {

    val product = MutableLiveData<List<Product>>()

    fun initialLoadData(){
        onRequestStart()

        onRequestFinish()
    }
}