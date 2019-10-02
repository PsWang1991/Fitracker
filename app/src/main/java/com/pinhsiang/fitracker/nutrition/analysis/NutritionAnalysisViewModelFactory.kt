package com.pinhsiang.fitracker.nutrition.analysis

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NutritionAnalysisViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionAnalysisViewModel::class.java)) {
            return NutritionAnalysisViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}