package com.pinhsiang.fitracker.util

import android.graphics.drawable.Drawable
import com.pinhsiang.fitracker.FitrackerApplication

object Util {

    fun getString(resourceId: Int): String {
        return FitrackerApplication.appContext.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return FitrackerApplication.appContext.getColor(resourceId)
    }

    fun getDrawable(resourceId: Int): Drawable? {
        return FitrackerApplication.appContext.getDrawable(resourceId)
    }
}
