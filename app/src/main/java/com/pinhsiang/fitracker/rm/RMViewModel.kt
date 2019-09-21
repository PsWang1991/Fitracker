package com.pinhsiang.fitracker.rm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.digits


class RMViewModel(val app: Application) : AndroidViewModel(app) {

    val calculateDone = MutableLiveData<Boolean>().apply {
        value = false
    }

    val inputLift = MutableLiveData<String>().apply {
        value = ""
    }

    val inputRepeats = MutableLiveData<String>().apply {
        value = ""
    }

    private val _lift1RMValue = MutableLiveData<Float>()
    val lift1RMValue: LiveData<Float>
        get() = _lift1RMValue

    private val _lift1RM = MutableLiveData<String>()
    val lift1RM: LiveData<String>
        get() = _lift1RM

    private val _lift2RM = MutableLiveData<String>()
    val lift2RM: LiveData<String>
        get() = _lift2RM

    private val _lift4RM = MutableLiveData<String>()
    val lift4RM: LiveData<String>
        get() = _lift4RM

    private val _lift6RM = MutableLiveData<String>()
    val lift6RM: LiveData<String>
        get() = _lift6RM

    private val _lift8RM = MutableLiveData<String>()
    val lift8RM: LiveData<String>
        get() = _lift8RM

    private val _lift10RM = MutableLiveData<String>()
    val lift10RM: LiveData<String>
        get() = _lift10RM

    private val _lift12RM = MutableLiveData<String>()
    val lift12RM: LiveData<String>
        get() = _lift12RM

    private val _lift20RM = MutableLiveData<String>()
    val lift20RM: LiveData<String>
        get() = _lift20RM

    fun calculate() {
        if (checkInputLiftFormat() && checkInputRepeatsFormat()) {
            if (inputRepeats.value!!.toInt() in 1..36) {
                calculateDone.value = true
                _lift1RMValue.value = inputLift.value?.toFloat()?.times((36 / (37 - inputRepeats.value!!.toInt())).toFloat())
                _lift1RM.value = _lift1RMValue.value.toString().format("%.02f")
                _lift2RM.value = (_lift1RMValue.value!! * 0.97f).digits(2)
                _lift4RM.value = (_lift1RMValue.value!! * 0.92f).digits(2)
                _lift6RM.value = (_lift1RMValue.value!! * 0.86f).digits(2)
                _lift8RM.value = (_lift1RMValue.value!! * 0.81f).digits(2)
                _lift10RM.value = (_lift1RMValue.value!! * 0.75f).digits(2)
                _lift12RM.value = (_lift1RMValue.value!! * 0.70f).digits(2)
                _lift20RM.value = (_lift1RMValue.value!! * 0.50f).digits(2)
            } else {
                calculateDone.value = false
                Toast.makeText(FitrackerApplication.appContext, "Repeats could not greater than 36.", Toast.LENGTH_SHORT).show()
            }
        } else {
            calculateDone.value = false
            Toast.makeText(FitrackerApplication.appContext, "Wrong format of input!!!", Toast.LENGTH_SHORT).show()
        }
        Log.i(TAG, "inputLift = ${inputLift.value}")
        Log.i(TAG, "inputRepeats = ${inputRepeats.value}")
        val liftOK = checkInputLiftFormat()
        val repeatsOK = checkInputRepeatsFormat()
        Log.i(TAG, "liftOK = $liftOK")
        Log.i(TAG, "repeatsOK = $repeatsOK")
    }

    private fun checkInputLiftFormat(): Boolean {
        val recordedValueFormat = "[0-9]{0,3}([.][0-9]{1,2})?".toRegex()
        return recordedValueFormat.matches(inputLift.value.toString())
    }

    private fun checkInputRepeatsFormat(): Boolean {
        val recordedValueFormat = "[0-9]+".toRegex()
        return recordedValueFormat.matches(inputRepeats.value.toString())
    }

}