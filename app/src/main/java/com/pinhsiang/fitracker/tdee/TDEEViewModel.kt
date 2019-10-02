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

const val MALE = true
const val FEMALE = false
const val MALE_BIAS = 5f
const val FEMALE_BIAS = -161f
const val COEFFICIENT_AGE = 4.92f
const val COEFFICIENT_WEIGHT = 9.99f
const val COEFFICIENT_HEIGHT = 6.25f

class TDEEViewModel : ViewModel() {

    val calculateDone = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var gender: Boolean? = null

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
    private val _displayBMR = MutableLiveData<String>()
    val displayBMR: LiveData<String>
        get() = _displayBMR

    // SED = Sedentary
    private val _displaySED = MutableLiveData<String>()
    val displaySED: LiveData<String>
        get() = _displaySED

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


    fun setGenderMale() {
        gender = MALE
        Log.i(TAG, "gender = $gender")
    }

    fun setGenderFemale() {
        gender = FEMALE
        Log.i(TAG, "gender = $gender")
    }

    fun calculate() {
        if (gender != null) {
            if (checkInputAgeFormat() && checkInputWeightFormat() && checkInputHeightFormat()) {
                val genderBias = if (gender == true) MALE_BIAS else FEMALE_BIAS
                val bmr =
                    inputHeight.value!!.toFloat().times(COEFFICIENT_HEIGHT) +
                            inputWeight.value!!.toFloat().times(COEFFICIENT_WEIGHT) -
                            inputAge.value!!.toFloat().times(COEFFICIENT_AGE) + genderBias
                Log.i(TAG, "bmr = $bmr")
                Log.i(TAG, "Result height = ${inputHeight.value!!.toFloat().times(COEFFICIENT_HEIGHT)}")
                Log.i(TAG, "Result weight = ${inputWeight.value!!.toFloat().times(COEFFICIENT_WEIGHT)}")
                Log.i(TAG, "Result age = ${inputAge.value!!.toFloat().times(COEFFICIENT_AGE)}")
                Log.i(TAG, "genderBias = $genderBias")

                _displayBMR.value = String.format(getString(R.string.format_tdee), bmr.toInt())

                val tdeeSed = if (gender == true) 1.2f else 1.1f
                _displaySED.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeSed)).toInt())

                val tdeeL = if (gender == true) 1.375f else 1.275f
                _displayL.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeL)).toInt())

                val tdeeM = if (gender == true) 1.55f else 1.35f
                _displayM.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeM)).toInt())

                val tdeeH = if (gender == true) 1.725f else 1.525f
                _displayH.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeH)).toInt())

                val tdeeAthlete = if (gender == true) 1.9f else 1.8f
                _displayAthlete.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeAthlete)).toInt())

                calculateDone.value = true
            } else {
                calculateDone.value = false
                Log.i(TAG, "Something extraordinary.")
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