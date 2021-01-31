package com.sehatq.testapplication.core.network.rx

import com.sehatq.testapplication.core.network.ResponseErrorHandler
import com.sehatq.testapplication.core.network.ServiceResponseHandler
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class ResponseCompletable constructor(private val compositeDisposable: CompositeDisposable):
    CompletableObserver {

    private val serviceResponseHandler = ServiceResponseHandler<String>()

    override fun onComplete() {
        onResponseSuccess()
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        val error = ResponseErrorHandler().handleResponseError(e)
        onResponseError("${error.message}", error.code)
    }

    abstract fun onResponseSuccess()
    abstract fun onResponseError(msg: String, code: Int)
}