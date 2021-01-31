package com.sehatq.testapplication.core.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.sehatq.testapplication.core.network.base.BaseResponse
import com.sehatq.testapplication.core.network.response.ErrorResponse
import com.sehatq.testapplication.core.util.Constants
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class ResponseErrorHandler {
    fun handleResponseError(e: Throwable): ErrorResponse {
        e.printStackTrace()
        val error = ErrorResponse()
        when (e) {
            is HttpException -> {
                //Have Error Message
                val responseBody = (e).response().errorBody()
                val errorResponse = parseError(responseBody)
                if (errorResponse != null) {
                    Constants.log(
                        "[SERVICE_RESPONSE_FAILURE] " + "\n" +
                                " code:" + errorResponse.code + "\n" +
                                " HTTP code:" + e.code() + "\n" +
                                " message:" + errorResponse.message + "\n"
                    )

                    when (e.code()) {
                        302 ->
                            error.message = "Hotspot login required"
                        else ->
                            error.message = errorResponse.message ?: e.message()
                    }
                    error.code = errorResponse.code
                } else {
                    error.message = "Something wrong with our server. Please try again later."
                }
            }
            is JsonSyntaxException ->
                //Error Parsing
                error.message = "Something wrong with response data."
            is SocketTimeoutException ->
                //Network Timeout
                error.message = "Request Timeout."
            is IOException ->
                //Network Error
                error.message = "No internet connection. please try again letter."
            else -> {
                //General Error
                error.code = 599
                error.message = "Sorry, problem server. please try again letter."
            }
        }
        return error
    }

    private fun parseError(errorBody: ResponseBody?): ErrorResponse? {
        val converter = NetworkClient.retrofit
            .responseBodyConverter<ErrorResponse>(ErrorResponse::class.java, arrayOfNulls(0))

        val error: ErrorResponse?

        try {
            error = converter.convert(errorBody!!)
        } catch (e: IOException) {
            return ErrorResponse()
        }

        return error
    }
}

class ServiceResponseHandler<T> {

    fun handleResponseSuccess(response: BaseResponse<T>) : T? {
        val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting().create()
        try { Constants.log("[SERVICE_RESPONSE_OK] " + "\n" +
                "code : " + response.code() + "\n" +
                "message : " + response.message() + "\n" +
                "response body : " + gson.toJson(response.data()) ) }
        catch (e: Exception){}

        return response.data()
    }

}