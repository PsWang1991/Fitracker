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

    val calculateDone = MutableLiveData<Boolean>().apply {
        value = false
    }

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
    }

    fun setGenderFemale() {
        gender = FEMALE
    }

    fun calculate() {

        if (gender != null) {

            if (checkInputAgeFormat() && checkInputWeightFormat() && checkInputHeightFormat()) {

                val genderBias = when (gender) {
                    MALE -> BIAS_MALE
                    else -> BIAS_FEMALE
                }

                // bmr = basal metabolic rate
                val bmr =
                    inputHeight.value!!.toFloat().times(COEFFICIENT_HEIGHT) +
                            inputWeight.value!!.toFloat().times(COEFFICIENT_WEIGHT) -
                            inputAge.value!!.toFloat().times(COEFFICIENT_AGE) + genderBias

                _displayBMR.value = String.format(getString(R.string.format_tdee), bmr.toInt())

                val tdeeSed = when (gender) {
                    MALE -> SEDENTARY_MALE
                    else -> SEDENTARY_FEMALE
                }
                _displaySED.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeSed)).toInt())

                val tdeeL = when (gender) {
                    MALE -> LIGHT_EXERCISE_MALE
                    else -> LIGHT_EXERCISE_FEMALE
                }
                _displayL.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeL)).toInt())

                val tdeeM = when (gender) {
                    MALE -> MODERATE_EXERCISE_MALE
                    else -> MODERATE_EXERCISE_FEMALE
                }
                _displayM.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeM)).toInt())

                val tdeeH = when (gender) {
                    MALE -> HEAVY_EXERCISE_MALE
                    else -> HEAVY_EXERCISE_FEMALE
                }
                _displayH.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeH)).toInt())

                val tdeeAthlete = when (gender) {
                    MALE -> ATHLETE_MALE
                    else -> ATHLETE_FEMALE
                }
                _displayAthlete.value = String.format(getString(R.string.format_tdee), (bmr.times(tdeeAthlete)).toInt())

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

    companion object {

        /**
         *  Gender
         */
        const val MALE = 0
        const val FEMALE = 1

        /**
         *  Coefficients
         */
        const val BIAS_MALE = 5f
        const val BIAS_FEMALE = -161f
        const val COEFFICIENT_AGE = 4.92f
        const val COEFFICIENT_WEIGHT = 9.99f
        const val COEFFICIENT_HEIGHT = 6.25f

        const val SEDENTARY_MALE = 1.2f
        const val SEDENTARY_FEMALE = 1.1f
        const val LIGHT_EXERCISE_MALE = 1.375f
        const val LIGHT_EXERCISE_FEMALE = 1.275f
        const val MODERATE_EXERCISE_MALE = 1.55f
        const val MODERATE_EXERCISE_FEMALE = 1.35f
        const val HEAVY_EXERCISE_MALE = 1.725f
        const val HEAVY_EXERCISE_FEMALE = 1.525f
        const val ATHLETE_MALE = 1.9f
        const val ATHLETE_FEMALE = 1.8f
    }
}