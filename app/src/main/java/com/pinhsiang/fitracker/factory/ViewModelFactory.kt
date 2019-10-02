package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.recommended.RecommendedViewModel
import com.pinhsiang.fitracker.rm.RMViewModel
import com.pinhsiang.fitracker.tdee.TDEEViewModel
import com.pinhsiang.fitracker.timer.TimerViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(RecommendedViewModel::class.java) ->
                    RecommendedViewModel()

                isAssignableFrom(RMViewModel::class.java) ->
                    RMViewModel()

                isAssignableFrom(TDEEViewModel::class.java) ->
                    TDEEViewModel()

                isAssignableFrom(TimerViewModel::class.java) ->
                    TimerViewModel()

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}