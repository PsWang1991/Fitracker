package com.pinhsiang.fitracker.ext

import android.app.Activity
import com.pinhsiang.fitracker.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    return ViewModelFactory()
}