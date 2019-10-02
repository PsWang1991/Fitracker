package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.InBody
import com.pinhsiang.fitracker.inbody.record.InBodyRecordViewModel

class InBodyRecordViewModelFactory(
    private val selectedInBody: InBody

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(InBodyRecordViewModel::class.java) ->
                    InBodyRecordViewModel(selectedInBody)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}