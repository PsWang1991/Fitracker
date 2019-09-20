package com.pinhsiang.fitracker.rm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RMViewModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RMViewModel::class.java)) {
            return RMViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}