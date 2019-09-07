package com.pinhsiang.fitracker.nutrition.analysis

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NutritionAnalysisViewModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionAnalysisViewModel::class.java)) {
            return NutritionAnalysisViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}