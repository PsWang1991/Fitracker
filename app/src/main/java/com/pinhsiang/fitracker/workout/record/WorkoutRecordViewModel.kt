package com.pinhsiang.fitracker.workout.record

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
import com.pinhsiang.fitracker.timestampToString
import com.pinhsiang.fitracker.util.Util.getDrawable
import com.pinhsiang.fitracker.util.Util.getString
import org.threeten.bp.LocalDate
import java.sql.Timestamp
import java.util.*

const val TAG = "Fitracker"

class WorkoutRecordViewModel(selectedWorkout: Workout, app: Application) : AndroidViewModel(app) {

    // Internal and external motion list
    private val _motionList = mutableListOf<Motion>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    val motionList = MutableLiveData<List<Motion>>()

    init {
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
        Log.i(TAG, "selectedWorkout = $selectedWorkout")
        Log.i(TAG, "Date = ${selectedWorkout.time.timestampToString()}")
        Log.i(TAG, "Motion = ${selectedWorkout.motion}")
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
    }

}

