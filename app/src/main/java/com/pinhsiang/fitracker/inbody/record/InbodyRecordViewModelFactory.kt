package com.pinhsiang.fitracker.inbody.record

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Inbody

class InbodyRecordViewModelFactory(
    private val selectedInbody: Inbody,
    private val application: Application

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InbodyRecordViewModel::class.java)) {
            return InbodyRecordViewModel(selectedInbody, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}