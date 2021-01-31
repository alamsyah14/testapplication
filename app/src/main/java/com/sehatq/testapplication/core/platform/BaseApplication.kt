package com.sehatq.testapplication.core.platform

import android.app.Application
import com.sehatq.testapplication.core.util.Cache

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Cache.context(this.applicationContext)
    }
}