package com.pinhsiang.fitracker.tdee

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.util.Util.getString

class TdeeViewModel : ViewModel() {

    lateinit var tdee: Tdee

    private var gender: Int? = null

    val inputAge = MutableLiveData<String>().apply {
        value = ""
    }

    val inputWeight = MutableLiveData<String>().apply {
        value = ""
    }

    val inputHeight = MutableLiveData<String>().apply {
        value = ""
    }

    // BMR = Basal Metabolic Rate
    private val _displayBmr = MutableLiveData<String>()
    val displayBmr: LiveData<String>
        get() = _displayBmr

    // SED = Sedentary
    private val _displaySed = MutableLiveData<String>()
    val displaySed: LiveData<String>
        get() = _displaySed

    // L = Light Exercise
    private val _displayL = MutableLiveData<String>()
    val displayL: LiveData<String>
        get() = _displayL

    // M = Moderate Exercise
    private val _displayM = MutableLiveData<String>()
    val displayM: LiveData<String>
        get() = _displayM

    // H = Heavy Exercise
    private val _displayH = MutableLiveData<String>()
    val displayH: LiveData<String>
        get() = _displayH

    private val _displayAthlete = MutableLiveData<String>()
    val displayAthlete: LiveData<String>
        get() = _displayAthlete

    val calculateDone = MutableLiveData<Boolean>().apply {
        value = false
    }


    fun setGenderMale() {
        gender = Tdee.MALE
    }

    fun setGenderFemale() {
        gender = Tdee.FEMALE
    }

    fun calculate() {

        if (gender != null) {

            if (checkInputAgeFormat() && checkInputWeightFormat() && checkInputHeightFormat()) {

                tdee = Tdee(
                    gender = gender!!,
                    age = inputAge.value!!.toFloat(),
                    height = inputHeight.value!!.toFloat(),
                    weight = inputWeight.value!!.toFloat()
                )

                _displayBmr.value = String.format(getString(R.string.format_tdee), tdee.bmr())
                _displaySed.value = String.format(getString(R.string.format_tdee), tdee.sedentary())
                _displayL.value = String.format(getString(R.string.format_tdee), tdee.lightExercise())
                _displayM.value = String.format(getString(R.string.format_tdee), tdee.moderateExercise())
                _displayH.value = String.format(getString(R.string.format_tdee), tdee.heavyExercise())
                _displayAthlete.value = String.format(getString(R.string.format_tdee), tdee.athlete())

                calculateDone.value = true

            } else {

                calculateDone.value = false
                Toast.makeText(FitrackerApplication.appContext, "Something extraordinary.", Toast.LENGTH_SHORT).show()
            }

        } else {

            calculateDone.value = false
            Toast.makeText(FitrackerApplication.appContext, "Gender is unknown.", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "Gender is unknown.")
        }
    }

    private fun checkInputAgeFormat(): Boolean {
        val recordedValueFormat = "[0-9]+".toRegex()
        return recordedValueFormat.matches(inputAge.value.toString())
    }

    private fun checkInputWeightFormat(): Boolean {
        val recordedValueFormat = "[0-9]{1,4}([.][0-9]{1,2})?".toRegex()
        return recordedValueFormat.matches(inputWeight.value.toString())
    }

    private fun checkInputHeightFormat(): Boolean {
        val recordedValueFormat = "[0-9]{1,4}([.][0-9]{1,2})?".toRegex()
        return recordedValueFormat.matches(inputHeight.value.toString())
    }
}