package com.pinhsiang.fitracker.recommended

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecommendedViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendedViewModel::class.java)) {
            return RecommendedViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}