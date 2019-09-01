package com.pinhsiang.fitracker.nutrition

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NutritionViewModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionViewModel::class.java)) {
            return NutritionViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}