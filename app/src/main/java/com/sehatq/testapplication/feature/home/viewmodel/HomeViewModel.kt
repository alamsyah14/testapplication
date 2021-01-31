package com.sehatq.testapplication.feature.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sehatq.testapplication.core.network.response.*
import com.sehatq.testapplication.core.network.service.HomeService
import com.sehatq.testapplication.core.platform.BaseViewModel
import com.sehatq.testapplication.core.util.Cache
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : BaseViewModel() {

    private val services = HomeService.instance()

    val listCategory    = MutableLiveData<List<Category>>()
    val listProduct     = MutableLiveData<List<Product>>()

    fun initialLoadData(isSilent: Boolean = false) {
        if (!isSilent) onRequestStart()
        services.getHomeData().enqueue(object : retrofit2.Callback<List<Data>> {
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                if (!isSilent) onRequestFinish()
                errorResponse.postValue(ErrorResponse(t.hashCode(),t.localizedMessage))
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    val home = response.body()?.first()
                    val categoryList = home?.data?.category ?: listOf()
                    val productList = home?.data?.productPromo ?: listOf()
                    listCategory.postValue(categoryList)
                    listProduct.postValue(productList)
                    Cache.saveProductList(RespondProduct(product = productList))
                    if (!isSilent) onRequestFinish()
                }
            }
        })
    }

}