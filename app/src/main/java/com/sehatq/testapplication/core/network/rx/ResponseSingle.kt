package com.sehatq.testapplication.core.network.rx

import com.sehatq.testapplication.core.network.ResponseErrorHandler
import com.sehatq.testapplication.core.network.ServiceResponseHandler
import com.sehatq.testapplication.core.network.base.BaseResponse
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class ResponseSingle <T> constructor(
    private val compositeDisposable: CompositeDisposable,
    private val dontThrowErrorIsEmpty: Boolean = false
): SingleObserver<BaseResponse<T>> {

    private val serviceResponseHandler = ServiceResponseHandler<T>()

    override fun onSuccess(response: BaseResponse<T>) {
        val data = serviceResponseHandler.handleResponseSuccess(response)
        data?.let {
            onResponseSuccess(it, response.message(), response.code())
        } ?: run {
            if (dontThrowErrorIsEmpty)
                onResponseSuccess(null, response.message(), response.code())
            else
                onResponseError(response.message(), response.code())
        }
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onError(e: Throwable) {
        val error = ResponseErrorHandler().handleResponseError(e)
        onResponseError("${error.message}", error.code)
    }

    abstract fun onResponseSuccess(data: T?, msg: String, code: Int)
    abstract fun onResponseError(msg: String, code: Int)
}