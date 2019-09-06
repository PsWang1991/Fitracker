package com.pinhsiang.fitracker.inbody.record

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"

class InbodyRecordViewModel(val selectedInbody: Inbody, app: Application) : AndroidViewModel(app) {


    val selectedDate = selectedInbody.time.timestampToDate()

    private var nutritionTemp = selectedInbody

//    val titleRecord = MutableLiveData<String>().apply {
//        value = selectedNutrition.title
//    }
//
//    val proteinRecord = MutableLiveData<Int>().apply {
//        value = selectedNutrition.protein
//    }
//
//    val carbohydrateRecord = MutableLiveData<Int>().apply {
//        value = selectedNutrition.carbohydrate
//    }
//
//    val fatRecord = MutableLiveData<Int>().apply {
//        value = selectedNutrition.fat
//    }

    init {
        Log.i(TAG, "**********   InbodyRecordViewModel   *********")
        Log.i(TAG, "Selected Inbody = $selectedInbody")
        Log.i(TAG, "Skeletal Muscle = ${selectedInbody.skeletalMuscle}%")
        Log.i(TAG, "Date = ${selectedInbody.time.timestampToString()}")
        Log.i(TAG, "**********   InbodyRecordViewModel   *********")
    }

    fun saveData() {
//        nutritionTemp = Nutrition(
//            time = selectedNutrition.time,
//            title = titleRecord.value!!,
//            protein = proteinRecord.value!!,
//            carbohydrate = carbohydrateRecord.value!!,
//            fat = fatRecord.value!!
//        )
//        Log.i(TAG, "nutritionTemp = $nutritionTemp")
    }

//    fun proteinPlus1() {
//        proteinRecord.value = proteinRecord.value?.plus(1)
//    }
//
//    fun proteinMinus1() {
//        proteinRecord.value = proteinRecord.value?.minus(1)
//    }
//
//    fun carbohydratePlus1() {
//        carbohydrateRecord.value = carbohydrateRecord.value?.plus(1)
//    }
//
//    fun carbohydrateMinus1() {
//        carbohydrateRecord.value = carbohydrateRecord.value?.minus(1)
//    }
//
//    fun fatPlus1() {
//        fatRecord.value = fatRecord.value?.plus(1)
//    }
//
//    fun fatMinus1() {
//        fatRecord.value = fatRecord.value?.minus(1)
//    }
//
//    fun setProteinRecordTo0() {
//        proteinRecord.value = 0
//    }
//
//    fun setCarbohydrateRecordTo0() {
//        carbohydrateRecord.value = 0
//    }
//
//    fun setFatRecordRecordTo0() {
//        fatRecord.value = 0
//    }
}