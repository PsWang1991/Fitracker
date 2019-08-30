package com.pinhsiang.fitracker.workout

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import java.util.*

const val TAG = "Fitracker"
const val CALENDAR_MONTH_OFFSET = 1

class WorkoutViewModel(app: Application) : AndroidViewModel(app) {

    // Get current date
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET
    private val date = Calendar.getInstance().get(Calendar.DATE)

    // Internal and external workout data list
    private val _workoutList = mutableListOf<Workout>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    val workoutList = MutableLiveData<List<Workout>>()

    init {
        createMockWorkoutData()
        getWorkoutDataByDate(currentYear, currentMonth, date)
    }

    fun getWorkoutDataByDate(year: Int, month: Int, date: Int) {
        workoutList.value = _workoutList.filter {
            it.year == year &&
            it.month == month &&
            it.date == date
        }
        Log.i(TAG, "$year/$month/$date")
        Log.i(TAG, "_workoutList = $_workoutList")
        Log.i(TAG, "workoutList = ${workoutList.value}")
    }

    private fun createMockWorkoutData() {
        val w1set1 = Sets(20, 10)
        val w1set2 = Sets(30, 8)
        val w1set3 = Sets(25, 15)
        val w1set = listOf(w1set1, w1set2, w1set3)
        val workout1 = Workout(
            year = Calendar.getInstance().get(Calendar.YEAR),
            month = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET,
            date = Calendar.getInstance().get(Calendar.DATE),
            motion = "Bench Press",
            sets = w1set
        )

        val w2set1 = Sets(20, 15)
        val w2set2 = Sets(60, 10)
        val w2set3 = Sets(85, 8)
        val w2set = listOf(w2set1, w2set2, w2set3)
        val workout2 = Workout(
            year = Calendar.getInstance().get(Calendar.YEAR),
            month = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET,
            date = Calendar.getInstance().get(Calendar.DATE),
            motion = "Deadlift",
            sets = w2set
        )

        val w3set1 = Sets(20, 10)
        val w3set2 = Sets(50, 8)
        val w3set3 = Sets(70, 6)
        val w3set = listOf(w3set1, w3set2, w3set3)
        val workout3 = Workout(
            year = Calendar.getInstance().get(Calendar.YEAR),
            month = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET,
            date = Calendar.getInstance().get(Calendar.DATE) - 1,
            motion = "Squat",
            sets = w3set
        )

        val w4set1 = Sets(20, 15)
        val w4set2 = Sets(60, 10)
        val w4set3 = Sets(85, 8)
        val w4set = listOf(w4set1, w4set2, w4set3)
        val workout4 = Workout(
            year = Calendar.getInstance().get(Calendar.YEAR),
            month = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET,
            date = Calendar.getInstance().get(Calendar.DATE) - 1,
            motion = "Deadlift",
            sets = w4set
        )

        val dataList = listOf(workout1, workout2, workout3, workout4)
        _workoutList.addAll(dataList)
    }
}