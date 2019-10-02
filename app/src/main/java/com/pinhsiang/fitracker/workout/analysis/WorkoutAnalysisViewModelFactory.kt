package com.pinhsiang.fitracker.workout.analysis

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkoutAnalysisViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutAnalysisViewModel::class.java)) {
            return WorkoutAnalysisViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}