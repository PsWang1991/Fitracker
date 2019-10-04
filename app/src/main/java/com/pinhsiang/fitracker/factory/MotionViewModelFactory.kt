package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.workout.motion.MotionViewModel

@Suppress("UNCHECKED_CAST")
class MotionViewModelFactory(
    private val dataTime: Long

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MotionViewModel::class.java) ->
                    MotionViewModel(dataTime)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}