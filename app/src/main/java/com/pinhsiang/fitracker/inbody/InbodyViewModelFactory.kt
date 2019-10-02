package com.pinhsiang.fitracker.inbody

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InbodyViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InbodyViewModel::class.java)) {
            return InbodyViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}