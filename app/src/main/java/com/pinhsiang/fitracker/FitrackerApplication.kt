package com.pinhsiang.fitracker

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

// Provides a global context for whole app
class FitrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this
        AndroidThreeTen.init(this)
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}