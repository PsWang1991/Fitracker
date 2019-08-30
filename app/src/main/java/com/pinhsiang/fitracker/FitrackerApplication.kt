package com.pinhsiang.fitracker

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlin.properties.Delegates

//This class provide a global context for whole app

class FitrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        AndroidThreeTen.init(this)

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}