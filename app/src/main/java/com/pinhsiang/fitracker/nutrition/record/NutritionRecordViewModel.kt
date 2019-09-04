package com.pinhsiang.fitracker.nutrition.record

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"

class NutritionRecordViewModel(selectedNutrition: Nutrition, app: Application) : AndroidViewModel(app) {


    private val nutritionTemp = selectedNutrition

    val titleRecord = MutableLiveData<String>().apply {
        value = ""
    }

    val proteinRecord = MutableLiveData<Int>().apply {
        value = 0
    }

    val carbohydrateRecord = MutableLiveData<Int>().apply {
        value = 0
    }

    val fatRecord = MutableLiveData<Int>().apply {
        value = 0
    }

    init {
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
        Log.i(TAG, "Selected Nutrition = $selectedNutrition")
        Log.i(TAG, "Title = ${selectedNutrition.title}")
        Log.i(TAG, "Date = ${selectedNutrition.time.timestampToString()}")
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
    }

    fun addData() {
        Log.i(TAG, "titleRecord = ${titleRecord.value}")
        Log.i(TAG, "proteinRecord = ${proteinRecord.value}")
        Log.i(TAG, "carbohydrateRecord = ${carbohydrateRecord.value}")
        Log.i(TAG, "fatRecord = ${fatRecord.value}")
    }

    fun proteinPlus1() {
        proteinRecord.value = proteinRecord.value?.plus(1)
    }

    fun proteinMinus1() {
        proteinRecord.value = proteinRecord.value?.minus(1)
    }

    fun carbohydratePlus1() {
        carbohydrateRecord.value = carbohydrateRecord.value?.plus(1)
    }

    fun carbohydrateMinus1() {
        carbohydrateRecord.value = carbohydrateRecord.value?.minus(1)
    }

    fun fatPlus1() {
        fatRecord.value = fatRecord.value?.plus(1)
    }

    fun fatMinus1() {
        fatRecord.value = fatRecord.value?.minus(1)
    }

    fun setProteinRecordTo0() {
        proteinRecord.value = 0
    }

    fun setCarbohydrateRecordTo0() {
        carbohydrateRecord.value = 0
    }

    fun setFatRecordRecordTo0() {
        fatRecord.value = 0
    }
}