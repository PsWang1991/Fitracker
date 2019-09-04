package com.pinhsiang.fitracker.nutrition.record

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.data.Workout

class NutritionRecordViewModelFactory(
    private val selectedNutrition: Nutrition,
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionRecordViewModel::class.java)) {
            return NutritionRecordViewModel(selectedNutrition, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}