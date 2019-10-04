package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.workout.record.WorkoutRecordViewModel

@Suppress("UNCHECKED_CAST")
class WorkoutRecordViewModelFactory(
    private val selectedWorkout: Workout

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(WorkoutRecordViewModel::class.java) ->
                    WorkoutRecordViewModel(selectedWorkout)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}