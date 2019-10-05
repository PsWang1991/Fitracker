package com.pinhsiang.fitracker.workout.motion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Motion
import com.pinhsiang.fitracker.util.Util.getDrawable
import com.pinhsiang.fitracker.util.Util.getString

class MotionViewModel(val dataTime: Long) : ViewModel() {

    private val _motionList = MutableLiveData<List<Motion>>()
    val motionList: LiveData<List<Motion>>
        get() = _motionList

    val selectedMotion = MutableLiveData<String>()

    init {
        createMotionList()
    }

    private fun createMotionList() {

        val motion1 = Motion(getDrawable(R.drawable.motion_bench_press), getString(R.string.bench_press))
        val motion2 = Motion(getDrawable(R.drawable.motion_deadlift), getString(R.string.deadlift))
        val motion3 = Motion(getDrawable(R.drawable.motion_squat), getString(R.string.squat))
        val dataList = listOf(motion1, motion2, motion3)

        _motionList.value = dataList
    }

    fun selectMotion(motionName: String) {
        selectedMotion.value = motionName
    }

    fun setMotionDone() {
        selectedMotion.value = null
    }
}