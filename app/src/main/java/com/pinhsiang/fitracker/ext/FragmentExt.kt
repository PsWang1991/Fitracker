package com.pinhsiang.fitracker.ext

import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    return ViewModelFactory()
}