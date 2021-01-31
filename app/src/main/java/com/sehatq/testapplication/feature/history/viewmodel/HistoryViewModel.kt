package com.sehatq.testapplication.feature.history.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseViewModel
import com.sehatq.testapplication.core.util.Cache

class HistoryViewModel : BaseViewModel() {

    val product = MutableLiveData<List<Product>>()

    fun initialLoadData(){
        onRequestStart()
        val respond = Cache.getPurchaseList
        product.postValue(respond?.product ?: listOf())
        onRequestFinish()
    }

}