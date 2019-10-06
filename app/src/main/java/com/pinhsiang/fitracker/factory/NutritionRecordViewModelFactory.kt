package com.pinhsiang.fitracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.nutrition.record.NutritionRecordViewModel

@Suppress("UNCHECKED_CAST")
class NutritionRecordViewModelFactory(
    private val selectedNutrition: Nutrition

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {

            when {

                isAssignableFrom(NutritionRecordViewModel::class.java) ->
                    NutritionRecordViewModel(selectedNutrition)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}