package com.pinhsiang.fitracker.inbody.analysis

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InbodyAnalysisViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InbodyAnalysisViewModel::class.java)) {
            return InbodyAnalysisViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}