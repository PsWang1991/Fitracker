package com.pinhsiang.fitracker.workout.record

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.ext.secondsIntToTime
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.ext.timestampToString
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.Util.getString
import java.util.*

class WorkoutRecordViewModel(val selectedWorkout: Workout) : ViewModel() {

    val selectedDate = selectedWorkout.time.timestampToDate()

    // Internal and external set list
    private val setListTemp = mutableListOf<Sets>()

    private val _setList = MutableLiveData<List<Sets>>()
    val setList: LiveData<List<Sets>>
        get() = _setList

    private val _addNewItem = MutableLiveData<Boolean>()
    val addNewItem: LiveData<Boolean>
        get() = _addNewItem

    val weightRecord = MutableLiveData<Int>().apply {
        value = 0
    }

    val repeatsRecord = MutableLiveData<Int>().apply {
        value = 0
    }

    val reviseMode = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var revisedDataIndex: Int = -1

    private val _dataUploading = MutableLiveData<Boolean>()
    val dataUploading: LiveData<Boolean>
        get() = _dataUploading

    private val _uploadDataDone = MutableLiveData<Boolean>()
    val uploadDataDone: LiveData<Boolean>
        get() = _uploadDataDone

    private val _restTimerStart = MutableLiveData<Boolean>().apply {
        value = false
    }
    val restTimerStart: LiveData<Boolean>
        get() = _restTimerStart

    val restTimerTime = MutableLiveData<String>().apply {
        value = "00:00"
    }

    lateinit var restTimer: Timer

    inner class RestTimerTask : TimerTask() {
        var currentTime = 0

        override fun run() {
            restTimerTime.postValue(currentTime.secondsIntToTime())
            currentTime += 1
        }
    }

    init {
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
        Log.i(TAG, "selectedWorkout = $selectedWorkout")
        Log.i(TAG, "Date = ${selectedWorkout.time.timestampToString()}")
        Log.i(TAG, "Motion = ${selectedWorkout.motion}")
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
    }

    fun addData() {
        if (weightRecord.value!! == 0 || repeatsRecord.value!! == 0) {
            Toast.makeText(FitrackerApplication.appContext, "Weight and repeats could not be zero.", Toast.LENGTH_SHORT)
                .show()
        } else {
            setListTemp.add(Sets(weightRecord.value!!, repeatsRecord.value!!))
            _setList.value = setListTemp
            _addNewItem.value = true
        }
        Log.i(TAG, "weightRecord = ${weightRecord.value}")
        Log.i(TAG, "repeatsRecord = ${repeatsRecord.value}")
//        Log.i(TAG, "${setList.value}")
    }

    fun addNewItemDone() {
        _addNewItem.value = false
    }

    fun endOfSetList(): Int {
        return setListTemp.size - 1
    }

    fun weightPlus5() {
        weightRecord.value = weightRecord.value?.plus(5)
    }

    fun weightMinus5() {
        weightRecord.value = weightRecord.value?.minus(5)
    }

    fun repeatsPlus1() {
        repeatsRecord.value = repeatsRecord.value?.plus(1)
    }

    fun repeatsMinus1() {
        repeatsRecord.value = repeatsRecord.value?.minus(1)
    }

    fun setWeightRecordTo0() {
        weightRecord.value = 0
    }

    fun setRepeatsRecordTo0() {
        repeatsRecord.value = 0
    }

    fun reviseItem(dataIndex: Int) {
        revisedDataIndex = dataIndex
        reviseMode.value = true
    }

    fun reviseItemDone() {
        revisedDataIndex = -1
        reviseMode.value = false
    }

    fun deleteSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp.removeAt(revisedDataIndex)
            _setList.value = setListTemp
            reviseItemDone()
        }
    }

    fun updateSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp[revisedDataIndex] = Sets(liftWeight = weightRecord.value!!, repeats = repeatsRecord.value!!)
            _setList.value = setListTemp
            reviseItemDone()
        }
    }

    fun uploadData() {
        when {
            restTimerStart.value == true ->
                Toast.makeText(
                    FitrackerApplication.appContext,
                    getString(R.string.stop_rest_timer_to_upload),
                    Toast.LENGTH_SHORT
                ).show()

            setListTemp.isNotEmpty() -> {

                val workoutToAdded = Workout(
                    time = selectedWorkout.time,
                    motion = selectedWorkout.motion,
                    maxWeight = setListTemp.maxBy { it.liftWeight }!!.liftWeight,
                    sets = setListTemp
                )

                _dataUploading.value = true

                FirebaseFirestore.getInstance()
                    .collection(USER)
                    .document(UserManager.userDocId!!)
                    .collection(WORKOUT)
                    .add(workoutToAdded)
                    .addOnFailureListener { exception ->

                        _dataUploading.value = false

                        Toast.makeText(
                            FitrackerApplication.appContext,
                            "Uploading failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w(TAG, "Error getting documents.", exception)
                    }
                    .addOnCompleteListener {

                        _dataUploading.value = false
                        _uploadDataDone.value = true

                        Toast.makeText(
                            FitrackerApplication.appContext,
                            "Data saving completed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            else ->

                Toast.makeText(
                    FitrackerApplication.appContext,
                    "Set data is empty.",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    fun uploadCompletely() {
        _uploadDataDone.value = false
    }

    fun startRestTimer() {
        _restTimerStart.value = true
        val restTimerTask = RestTimerTask()
        restTimer = object : Timer() {}
        restTimer.schedule(restTimerTask, 0, 1000)
        Log.i(TAG, "restTimerStart = ${restTimerStart.value}")
    }

    fun closeRestTimer() {
        restTimer.cancel()
        _restTimerStart.value = false
        Log.i(TAG, "restTimerStart = ${restTimerStart.value}")
    }

    fun resetRestTimer() {
        restTimer.cancel()
        val restTimerTask = RestTimerTask()
        restTimer = object : Timer() {}
        restTimer.schedule(restTimerTask, 0, 1000)
        Log.i(TAG, "Reset rest timer.")
    }
}