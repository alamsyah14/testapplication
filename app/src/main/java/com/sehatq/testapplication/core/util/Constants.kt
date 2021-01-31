package com.sehatq.testapplication.core.util

import android.util.Log
import com.sehatq.testapplication.BuildConfig

object Constants {
    const val BASE_URL                   = BuildConfig.BASE_URL
    const val IS_PRODUCTION : Boolean           = BuildConfig.ENVIROMENT == "production"
    const val INITIAL_OS_NAME : String          = "ANDROID"

    const val ARGUMENT_ACTION                   = "action"
    const val ARGUMENT_URL                      = "url"
    const val ARGUMENT_DATA                     = "data"
    const val ARGUMENT_METHOD                   = "method"

    const val ARGUMENT_SEARCH                   = "search"
    const val ARGUMENT_HISTORY                  = "history"

    fun log(msg: String) {
        Log.i("sehatq", msg)
    }
}