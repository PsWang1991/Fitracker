package com.pinhsiang.fitracker.workout

import android.app.Application
import android.util.Log
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import org.threeten.bp.LocalDate
import java.sql.Timestamp
import java.util.*

const val TAG = "Fitracker"
const val MILLISECOND_PER_DAY = 86400000L
const val MILLISECOND_PER_WEEK = MILLISECOND_PER_DAY * 7
const val MILLISECOND_PER_MONTH = MILLISECOND_PER_DAY * 30
const val ZERO_HOUR = "00:00:00"
const val BENCH_PRESS = "Bench Press"
const val DEADLIFT = "Deadlift"
const val SQUAT = "Squat"

class WorkoutViewModel(app: Application) : AndroidViewModel(app) {

    // Internal and external workout data list
    private val _workoutList = mutableListOf<Workout>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    val workoutList = MutableLiveData<List<Workout>>()

    init {
        createMockWorkoutData()
        getWorkoutDataByDate(LocalDate.now())
    }

    fun getWorkoutDataByDate(date: LocalDate) {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        workoutList.value = _workoutList.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
        Log.i(TAG, "_workoutList = $_workoutList")
        Log.i(TAG, "workoutList = ${workoutList.value}")
    }

    fun hasWorkoutData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return _workoutList.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    private fun createMockWorkoutData() {
        val w1set1 = Sets(20, 10)
        val w1set2 = Sets(30, 8)
        val w1set3 = Sets(25, 15)
        val w1set = listOf(w1set1, w1set2, w1set3)
        val workout1 = Workout(
            motion = BENCH_PRESS,
            sets = w1set
        )

        val w2set1 = Sets(20, 15)
        val w2set2 = Sets(60, 10)
        val w2set3 = Sets(85, 8)
        val w2set = listOf(w2set1, w2set2, w2set3)
        val workout2 = Workout(
            motion = DEADLIFT,
            sets = w2set
        )

        val w3set1 = Sets(20, 10)
        val w3set2 = Sets(50, 8)
        val w3set3 = Sets(70, 6)
        val w3set = listOf(w3set1, w3set2, w3set3)
        val workout3 = Workout(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            motion = SQUAT,
            sets = w3set
        )

        val w4set1 = Sets(20, 15)
        val w4set2 = Sets(60, 10)
        val w4set3 = Sets(85, 8)
        val w4set = listOf(w4set1, w4set2, w4set3)
        val workout4 = Workout(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            motion = DEADLIFT,
            sets = w4set
        )

        val w5set1 = Sets(20, 15)
        val w5set2 = Sets(20, 100)
        val w5set = listOf(w5set1, w5set2)
        val workout5 = Workout(
            time = System.currentTimeMillis() + MILLISECOND_PER_WEEK,
            motion = BENCH_PRESS,
            sets = w5set
        )

        val w6set1 = Sets(20, 15)
        val w6set2 = Sets(50, 10)
        val w6set3 = Sets(60, 8)
        val w6set = listOf(w6set1, w6set2, w6set3)
        val workout6 = Workout(
            time = System.currentTimeMillis() - MILLISECOND_PER_MONTH,
            motion = SQUAT,
            sets = w6set
        )

        val dataList = listOf(workout1, workout2, workout3, workout4, workout5, workout6)
        _workoutList.addAll(dataList)
    }
}