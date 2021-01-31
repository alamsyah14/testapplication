package com.sehatq.testapplication.core.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sehatq.testapplication.core.network.response.ErrorResponse
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var loading = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<ErrorResponse>()

    protected fun onRequestStart(){
        loading.value = true
    }

    protected fun onRequestFinish(){
        loading.value = false
    }

    open fun getLoading(): LiveData<Boolean> {
        return loading
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}