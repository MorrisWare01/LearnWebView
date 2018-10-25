package com.morrisware.android.learnwebview

import android.app.Application

class App : Application() {

    companion object {
        var INSTANCE: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }


}