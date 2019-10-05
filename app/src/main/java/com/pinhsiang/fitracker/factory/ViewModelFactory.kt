package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.LoginViewModel
import com.pinhsiang.fitracker.MainViewModel
import com.pinhsiang.fitracker.inbody.InBodyViewModel
import com.pinhsiang.fitracker.inbody.analysis.InBodyAnalysisViewModel
import com.pinhsiang.fitracker.nutrition.NutritionViewModel
import com.pinhsiang.fitracker.nutrition.analysis.NutritionAnalysisViewModel
import com.pinhsiang.fitracker.recommended.RecommendedViewModel
import com.pinhsiang.fitracker.rm.RmViewModel
import com.pinhsiang.fitracker.tdee.TdeeViewModel
import com.pinhsiang.fitracker.timer.TimerViewModel
import com.pinhsiang.fitracker.workout.WorkoutViewModel
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel()

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel()

                isAssignableFrom(WorkoutViewModel::class.java) ->
                    WorkoutViewModel()

                isAssignableFrom(WorkoutAnalysisViewModel::class.java) ->
                    WorkoutAnalysisViewModel()

                isAssignableFrom(InBodyViewModel::class.java) ->
                    InBodyViewModel()

                isAssignableFrom(InBodyAnalysisViewModel::class.java) ->
                    InBodyAnalysisViewModel()

                isAssignableFrom(NutritionViewModel::class.java) ->
                    NutritionViewModel()

                isAssignableFrom(NutritionAnalysisViewModel::class.java) ->
                    NutritionAnalysisViewModel()

                isAssignableFrom(RecommendedViewModel::class.java) ->
                    RecommendedViewModel()

                isAssignableFrom(RmViewModel::class.java) ->
                    RmViewModel()

                isAssignableFrom(TdeeViewModel::class.java) ->
                    TdeeViewModel()

                isAssignableFrom(TimerViewModel::class.java) ->
                    TimerViewModel()

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}