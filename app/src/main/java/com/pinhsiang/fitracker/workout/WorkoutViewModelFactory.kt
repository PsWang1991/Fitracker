package com.pinhsiang.fitracker.workout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkoutViewModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}