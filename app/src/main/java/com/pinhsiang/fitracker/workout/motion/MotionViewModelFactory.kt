package com.pinhsiang.fitracker.workout.motion

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MotionViewModelFactory(
    private val dataTime: Long,
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MotionViewModel::class.java)) {
            return MotionViewModel(dataTime, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}