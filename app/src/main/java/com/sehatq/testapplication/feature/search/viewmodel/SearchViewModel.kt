package com.sehatq.testapplication.feature.search.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.sehatq.testapplication.core.extention.isNull
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseViewModel
import com.sehatq.testapplication.core.util.Cache

class SearchViewModel: BaseViewModel() {

    var listProduct: List<Product> = listOf()
    var filterListProduct: MutableList<Product> = mutableListOf()

    val product = MutableLiveData<List<Product>>()

    val searchTextField = ObservableField<String>("")
    private val search: String get() {return searchTextField.get() ?: ""}

    fun reLoadProduct() {
        val respond = Cache.getProductList
        if (respond.isNull()) {
            emptyStateProduct()
            return
        }
        listProduct = respond?.product ?: listOf()
        if (!listProduct.isNullOrEmpty()) {
            if (!search.isNullOrEmpty()) {
                filterListProduct.clear()
                filterListProduct = listProduct.filter { it.title.toLowerCase().contains(search.toLowerCase()) }.toMutableList()
                product.postValue(filterListProduct.toList())
                return
            }
        }
        emptyStateProduct()
    }

    private fun emptyStateProduct(){ product.postValue(listOf()) }
}