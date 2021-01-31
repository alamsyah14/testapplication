package com.sehatq.testapplication.core.network.service

import com.sehatq.testapplication.core.network.NetworkClient
import com.sehatq.testapplication.core.network.base.BaseResponse
import com.sehatq.testapplication.core.network.response.Data
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.Call

interface HomeService {

    companion object {
        private var instance: HomeService? = null

        fun instance() : HomeService {
            instance?.let {
                return it
            }?: return createInstance()
        }

        private fun createInstance(): HomeService {
            instance = NetworkClient.createService(HomeService::class.java)
            return instance!!
        }

        fun removeInstance(){
            instance = null
        }
    }

    @GET("home")
    fun getFeaturedHome() : Single<BaseResponse<List<Data>>>

    @GET("home")
    fun getHomeData(): Call<List<Data>>


}