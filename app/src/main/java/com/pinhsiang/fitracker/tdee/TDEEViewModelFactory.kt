package com.pinhsiang.fitracker.tdee

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TDEEViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TDEEViewModel::class.java)) {
            return TDEEViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}