package com.pinhsiang.fitracker.workout.motion

import android.app.Application
import android.util.Log
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Motion
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.util.Util.getDrawable
import com.pinhsiang.fitracker.util.Util.getString
import org.threeten.bp.LocalDate
import java.sql.Timestamp
import java.util.*

const val TAG = "Fitracker"

class MotionViewModel(val dataTime: Long, app: Application) : AndroidViewModel(app) {

    // Internal and external motion list
    private val _motionList = mutableListOf<Motion>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    val motionList = MutableLiveData<List<Motion>>()

    val selectedMotion = MutableLiveData<String>()

    init {
        createMotionList()
        getMotionList()
    }

    private fun getMotionList() {
        motionList.value = _motionList
    }

    private fun createMotionList() {
        val motion1 = Motion(getDrawable(R.drawable.motion_bench_press), getString(R.string.bench_press))
        val motion2 = Motion(getDrawable(R.drawable.motion_deadlift), getString(R.string.deadlift))
        val motion3 = Motion(getDrawable(R.drawable.motion_squat), getString(R.string.squat))
        val dataList = listOf(motion1, motion2, motion3)
        _motionList.addAll(dataList)
    }

    fun setMotion(motionName: String) {
        selectedMotion.value = motionName
    }

    fun setMotionDone() {
        selectedMotion.value = null
    }
}

