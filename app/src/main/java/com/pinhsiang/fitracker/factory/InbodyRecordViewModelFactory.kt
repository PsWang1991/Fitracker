package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.inbody.record.InbodyRecordViewModel

class InbodyRecordViewModelFactory(
    private val selectedInbody: Inbody

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(InbodyRecordViewModel::class.java) ->
                    InbodyRecordViewModel(selectedInbody)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}