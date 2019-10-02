package com.pinhsiang.fitracker.rm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RMViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RMViewModel::class.java)) {
            return RMViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}