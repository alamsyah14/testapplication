package com.sehatq.testapplication.core.network.base

open class BaseResponse<T> {
    private val message: String? = null
    private val code: Int = 0
    private val data: T? = null

    fun data(): T? {
        return data
    }

    fun code(): Int = code

    fun message(): String = message ?: ""
}