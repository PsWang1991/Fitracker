package com.pinhsiang.fitracker.ext

import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.data.InBody
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.factory.*

fun Fragment.getVmFactory(): ViewModelFactory {
    return ViewModelFactory()
}

fun Fragment.getVmFactory(dataTime: Long): MotionViewModelFactory {
    return MotionViewModelFactory(dataTime)
}

fun Fragment.getVmFactory(selectedInBody: InBody): InBodyRecordViewModelFactory {
    return InBodyRecordViewModelFactory(selectedInBody)
}

fun Fragment.getVmFactory(selectedNutrition: Nutrition): NutritionRecordViewModelFactory {
    return NutritionRecordViewModelFactory(selectedNutrition)
}

fun Fragment.getVmFactory(selectedWorkout: Workout): WorkoutRecordViewModelFactory {
    return WorkoutRecordViewModelFactory(selectedWorkout)
}