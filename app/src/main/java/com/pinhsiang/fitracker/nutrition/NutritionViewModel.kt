package com.pinhsiang.fitracker.nutrition

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Nutrition
import org.threeten.bp.LocalDate
import java.sql.Timestamp

const val TAG = "Fitracker"
const val MILLISECOND_PER_DAY = 86400000L
const val MILLISECOND_PER_WEEK = MILLISECOND_PER_DAY * 7
const val MILLISECOND_PER_MONTH = MILLISECOND_PER_DAY * 30
const val ZERO_HOUR = "00:00:00"

class NutritionViewModel(val app: Application) : AndroidViewModel(app) {

    // Internal and external workout data list
    private val _nutritionList = mutableListOf<Nutrition>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    val nutritionList = MutableLiveData<List<Nutrition>>()

    var selectedDate: LocalDate? = null
    val today = LocalDate.now()

    var calendarExpanding = true

    // Handle navigation to motion fragment.
    private val _navigationToRecord = MutableLiveData<Boolean>()
    val navigationToRecord: LiveData<Boolean>
        get() = _navigationToRecord

    init {
        createMockNutritionData()
        getNutritionDataByDate(LocalDate.now())
    }

    fun addNewData() {
        _navigationToRecord.value = true
    }

    fun addNewDataDone() {
        _navigationToRecord.value = false
    }

    fun getNutritionDataByDate(date: LocalDate) {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        nutritionList.value = _nutritionList.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
        Log.i(TAG, "_nutritionList = $_nutritionList")
        Log.i(TAG, "nutritionList = ${nutritionList.value}")
    }

    fun hasNutritionData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return _nutritionList.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    private fun createMockNutritionData() {

        val nutrition1 = Nutrition(
            title = "Break Fast",
            protein = 12,
            carbohydrate = 20,
            fat = 5

        )

        val nutrition2 = Nutrition(
            title = "Lunch",
            protein = 25,
            carbohydrate = 50,
            fat = 10

        )


        val nutrition3 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            title = "Break Fast",
            protein = 8,
            carbohydrate = 20,
            fat = 0
        )

        val nutrition4 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            title = "Lunch",
            protein = 40,
            carbohydrate = 60,
            fat = 20
        )

        val nutrition5 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            title = "Dinner",
            protein = 35,
            carbohydrate = 35,
            fat = 10
        )

        val nutrition6 = Nutrition(
            time = System.currentTimeMillis() + MILLISECOND_PER_WEEK,
            title = "Break Fast",
            protein = 10,
            carbohydrate = 25,
            fat = 3
        )

        val nutrition7 = Nutrition(
            time = System.currentTimeMillis() + MILLISECOND_PER_WEEK,
            title = "Lunch",
            protein = 45,
            carbohydrate = 60,
            fat = 15
        )

        val nutrition8 = Nutrition(
            time = System.currentTimeMillis() + MILLISECOND_PER_WEEK,
            title = "Dinner",
            protein = 40,
            carbohydrate = 20,
            fat = 0
        )

        val nutrition9 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_MONTH,
            title = "Break Fast",
            protein = 10,
            carbohydrate = 25,
            fat = 3
        )

        val nutrition10 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_MONTH,
            title = "Lunch",
            protein = 45,
            carbohydrate = 60,
            fat = 15
        )

        val nutrition11 = Nutrition(
            time = System.currentTimeMillis() - MILLISECOND_PER_MONTH,
            title = "Dinner",
            protein = 40,
            carbohydrate = 20,
            fat = 0
        )

        val dataList = listOf(
            nutrition1,
            nutrition2,
            nutrition3,
            nutrition4,
            nutrition5,
            nutrition6,
            nutrition7,
            nutrition8,
            nutrition9,
            nutrition10,
            nutrition11
        )
        _nutritionList.addAll(dataList)
    }
}