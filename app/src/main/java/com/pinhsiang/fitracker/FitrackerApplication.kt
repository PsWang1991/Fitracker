package com.pinhsiang.fitracker

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

//This class provide a global context for whole app

class FitrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
//        appContext = applicationContext

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    companion object {
        lateinit var appContext: Context
            private set
//        var appContext: FitrackerApplication by Delegates.notNull()
    }
}