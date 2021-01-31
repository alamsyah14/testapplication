package com.sehatq.testapplication.core.network

import com.sehatq.testapplication.core.network.interceptor.AuthenticationInterceptor
import com.sehatq.testapplication.core.network.loging.ResponseHttpLogging
import com.sehatq.testapplication.core.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {

    private val builder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor(ResponseHttpLogging())
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val httpClient: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .followRedirects(false)
            .addInterceptor { chain ->
                val builderNew = chain.request().newBuilder()
                builderNew.addHeader("Content-Type", "application/json")
                builderNew.addHeader("Accept", "application/json")
                builderNew.addHeader("User-Device-Type", Constants.INITIAL_OS_NAME ?: "")
                builderNew.addHeader("User-Device-Name", android.os.Build.MODEL ?: "")
                builderNew.addHeader("User-Device-Manufacturer", android.os.Build.MANUFACTURER ?: "")
                builderNew.addHeader("User-Device-System", android.os.Build.VERSION.CODENAME ?: "")
                val requestNew = builderNew.build()
                chain.proceed(requestNew)
            }.apply { if (!Constants.IS_PRODUCTION) addInterceptor(loggingInterceptor) }
    }

    var retrofit: Retrofit = builder.client(httpClient.build()).build()

    fun <S> createService(serviceClass: Class<S>, accesToken: String = ""): S {

        return if (accesToken.isBlank()) retrofit.create(serviceClass)
        else {
            val authInterceptor = AuthenticationInterceptor(accesToken)
            if (!httpClient.interceptors().contains(authInterceptor)) {
                httpClient.addInterceptor(authInterceptor)
            }

            builder.client(httpClient.build())
            retrofit = builder.build()
            retrofit.create(serviceClass)
        }
    }
}