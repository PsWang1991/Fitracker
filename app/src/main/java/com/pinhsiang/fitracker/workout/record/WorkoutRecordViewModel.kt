package com.pinhsiang.fitracker.workout.record

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"

class WorkoutRecordViewModel(val selectedWorkout: Workout, app: Application) : AndroidViewModel(app) {

    // Internal and external set list
    private val setListTemp = mutableListOf<Sets>()

    private val _setList = MutableLiveData<List<Sets>>()
    val setList: LiveData<List<Sets>>
        get() = _setList

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

    init {
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
        Log.i(TAG, "selectedWorkout = $selectedWorkout")
        Log.i(TAG, "Date = ${selectedWorkout.time.timestampToString()}")
        Log.i(TAG, "Motion = ${selectedWorkout.motion}")
        Log.i(TAG, "**********   WorkoutRecordViewModel   *********")
        initSetList()
    }

    private fun initSetList() {

//        setListTemp.addAll(selectedWorkout.sets!!.asIterable())

//        setList.value = setListTemp

        val set1 = Sets(20, 15)
        val set2 = Sets(50, 12)
        val set3 = Sets(60, 12)
        val set4 = Sets(70, 10)
        val set5 = Sets(80, 8)
        val dataList = listOf(set1, set2, set3, set4, set5)
        setListTemp.addAll(dataList)
        _setList.value = setListTemp
    }

    fun addData() {
        Log.i(TAG, "weightRecord = ${weightRecord.value}")
        Log.i(TAG, "repeatsRecord = ${repeatsRecord.value}")
        setListTemp.add(Sets(weightRecord.value!!, repeatsRecord.value!!))
        _setList.value = setListTemp
//        Log.i(TAG, "${setList.value}")
    }

    fun weightPlus5() {
        weightRecord.value = weightRecord.value?.plus(5)
//        Log.i(TAG, "${weightRecord.value}")
    }

    fun weightMinus5() {
        weightRecord.value = weightRecord.value?.minus(5)
//        Log.i(TAG, "${weightRecord.value}")
    }

    fun repeatsPlus1() {
        repeatsRecord.value = repeatsRecord.value?.plus(1)
//        Log.i(TAG, "${repeatsRecord.value}")
    }

    fun repeatsMinus1() {
        repeatsRecord.value = repeatsRecord.value?.minus(1)
//        Log.i(TAG, "${repeatsRecord.value}")
    }

    fun setWeightRecordTo0() {
        weightRecord.value = 0
    }

    fun setRepeatsRecordTo0() {
        repeatsRecord.value = 0
    }

    fun reviseModeOn(dataIndex: Int) {
        revisedDataIndex = dataIndex
        reviseMode.value = true
    }

    private fun reviseModeOff() {
        reviseMode.value = false
    }

    fun deleteSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp.removeAt(revisedDataIndex)
            revisedDataIndex = -1
            _setList.value = setListTemp
            reviseModeOff()
        }
    }

    fun updateSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp[revisedDataIndex] = Sets(liftWeight = weightRecord.value!!, repeats = repeatsRecord.value!!)
            revisedDataIndex = -1
            _setList.value = setListTemp
            reviseModeOff()
        }
    }
}