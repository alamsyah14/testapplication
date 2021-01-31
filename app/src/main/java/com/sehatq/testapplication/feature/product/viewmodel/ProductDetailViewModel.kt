package com.sehatq.testapplication.feature.product.viewmodel

import com.sehatq.testapplication.core.event.SingleLiveEvent
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.network.response.RespondProduct
import com.sehatq.testapplication.core.platform.BaseViewModel
import com.sehatq.testapplication.core.util.Cache

class ProductDetailViewModel : BaseViewModel() {

    val successAdd = SingleLiveEvent<Unit>()
    private var listPurchase: MutableList<Product> = mutableListOf()

    fun addPurchaseProduct(product: Product) {
        onRequestStart()
        listPurchase.clear()
        listPurchase.add(product)
        val respond = Cache.getPurchaseList
        val purhase = respond?.product?.toMutableList() ?: mutableListOf()
        purhase.addAll(listPurchase)
        val request = RespondProduct(purhase.toList())
        Cache.savePurchaseList(request)
        successAdd.call()
        onRequestFinish()
    }
}