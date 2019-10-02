package com.pinhsiang.fitracker.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimerViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}