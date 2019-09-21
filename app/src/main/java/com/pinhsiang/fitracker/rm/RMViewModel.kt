package com.pinhsiang.fitracker.rm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.TAG


class RMViewModel(val app: Application) : AndroidViewModel(app) {

    val inputLift = MutableLiveData<Float>().apply {
        value = 0f
    }

    val inputRepeats = MutableLiveData<Int>().apply {
        value = 0
    }

    private val _lift1RM = MutableLiveData<Float>()
    val lift1RM: LiveData<Float>
        get() = _lift1RM

    private val _lift2RM = MutableLiveData<Float>()
    val lift2RM: LiveData<Float>
        get() = _lift2RM

    private val _lift4RM = MutableLiveData<Float>()
    val lift4RM: LiveData<Float>
        get() = _lift4RM

    private val _lift6RM = MutableLiveData<Float>()
    val lift6RM: LiveData<Float>
        get() = _lift6RM

    private val _lift8RM = MutableLiveData<Float>()
    val lift8RM: LiveData<Float>
        get() = _lift8RM

    private val _lift10RM = MutableLiveData<Float>()
    val lift10RM: LiveData<Float>
        get() = _lift10RM

    private val _lift12RM = MutableLiveData<Float>()
    val lift12RM: LiveData<Float>
        get() = _lift12RM

    private val _lift20RM = MutableLiveData<Float>()
    val lift20RM: LiveData<Float>
        get() = _lift20RM

    fun calculate() {
        Log.i(TAG, "inputLift = ${inputLift.value}")
        Log.i(TAG, "inputRepeats = ${inputRepeats.value}")
    }
}