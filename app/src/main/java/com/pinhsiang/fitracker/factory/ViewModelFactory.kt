package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.inbody.InbodyViewModel
import com.pinhsiang.fitracker.inbody.analysis.InbodyAnalysisViewModel
import com.pinhsiang.fitracker.nutrition.NutritionViewModel
import com.pinhsiang.fitracker.nutrition.analysis.NutritionAnalysisFragment
import com.pinhsiang.fitracker.recommended.RecommendedViewModel
import com.pinhsiang.fitracker.rm.RMViewModel
import com.pinhsiang.fitracker.tdee.TDEEViewModel
import com.pinhsiang.fitracker.timer.TimerViewModel
import com.pinhsiang.fitracker.workout.WorkoutViewModel
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(WorkoutViewModel::class.java) ->
                    WorkoutViewModel()

                isAssignableFrom(WorkoutAnalysisViewModel::class.java) ->
                    WorkoutAnalysisViewModel()

                isAssignableFrom(InbodyViewModel::class.java) ->
                    InbodyViewModel()

                isAssignableFrom(InbodyAnalysisViewModel::class.java) ->
                    InbodyAnalysisViewModel()

                isAssignableFrom(NutritionViewModel::class.java) ->
                    NutritionViewModel()

                isAssignableFrom(NutritionAnalysisFragment::class.java) ->
                    NutritionAnalysisFragment()

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