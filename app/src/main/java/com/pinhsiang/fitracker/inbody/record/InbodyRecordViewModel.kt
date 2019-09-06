package com.pinhsiang.fitracker.inbody.record

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"

class InbodyRecordViewModel(val selectedInbody: Inbody, app: Application) : AndroidViewModel(app) {


    val selectedDate = selectedInbody.time.timestampToDate()

    private var inbodyTemp = selectedInbody

    val bodyWeightRecord = MutableLiveData<String>().apply {
        value = selectedInbody.bodyWeight.toString()
    }

    val bodyFatRecord = MutableLiveData<String>().apply {
        value = selectedInbody.bodyFat.toString()
    }

    val skeletalMuscleRecord = MutableLiveData<String>().apply {
        value = selectedInbody.skeletalMuscle.toString()
    }

    // Check that is all format of recorded numbers are correct.
    private val _validBodyWeight = MutableLiveData<Boolean>()
    val validBodyWeight: LiveData<Boolean>
        get() = _validBodyWeight

    private val _validBodyFat = MutableLiveData<Boolean>()
    val validBodyFat: LiveData<Boolean>
        get() = _validBodyFat

    private val _validSkeletalMuscle = MutableLiveData<Boolean>()
    val validSkeletalMuscle: LiveData<Boolean>
        get() = _validSkeletalMuscle

    init {
        Log.i(TAG, "**********   InbodyRecordViewModel   *********")
        Log.i(TAG, "Selected Inbody = $selectedInbody")
        Log.i(TAG, "Skeletal Muscle = ${selectedInbody.skeletalMuscle}%")
        Log.i(TAG, "Date = ${selectedInbody.time.timestampToString()}")
        Log.i(TAG, "**********   InbodyRecordViewModel   *********")
        initRecordedValueValid()
    }

    private fun initRecordedValueValid() {
        _validBodyWeight.value = true
        _validBodyFat.value = true
        _validSkeletalMuscle.value = true
    }

    fun saveData() {
        if (checkBodyWeightFormat() && checkBodyFatFormat() && checkSkeletalMuscleFormat()) {
            _validBodyWeight.value = true
            _validBodyFat.value = true
            _validSkeletalMuscle.value = true
            inbodyTemp = Inbody(
                time = selectedInbody.time,
                bodyWeight = bodyWeightRecord.value?.toFloat()!!,
                bodyFat = bodyFatRecord.value?.toFloat()!!,
                skeletalMuscle = skeletalMuscleRecord.value?.toFloat()!!
            )
            Log.i(TAG, "inbodyTemp = $inbodyTemp")
        } else {
            when {
                !checkBodyWeightFormat() -> _validBodyWeight.value = false
                else -> _validBodyWeight.value = true
            }
            when {
                !checkBodyFatFormat() -> _validBodyFat.value = false
                else -> _validBodyFat.value = true
            }
            when {
                !checkSkeletalMuscleFormat() -> _validSkeletalMuscle.value = false
                else -> _validSkeletalMuscle.value = true
            }
            Log.i(TAG, "_validBodyWeight = ${_validBodyWeight.value}")
            Log.i(TAG, "_validBodyFat = ${_validBodyFat.value}")
            Log.i(TAG, "_validSkeletalMuscle = ${_validSkeletalMuscle.value}")
        }
    }

    private fun checkBodyWeightFormat(): Boolean {
        val recordedValueFormat = "[0-9]{0,3}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(bodyWeightRecord.value.toString())
    }

    private fun checkBodyFatFormat(): Boolean {
        val recordedValueFormat = "[0-9]{0,2}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(bodyFatRecord.value.toString())
    }

    private fun checkSkeletalMuscleFormat(): Boolean {
        val recordedValueFormat = "[0-9]{0,2}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(skeletalMuscleRecord.value.toString())
    }
}