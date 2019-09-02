package com.pinhsiang.fitracker.workout.record

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Workout

class WorkoutRecordViewModelFactory(
    private val selectedWorkout: Workout,
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutRecordViewModel::class.java)) {
            return WorkoutRecordViewModel(selectedWorkout, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}