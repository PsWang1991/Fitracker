package com.pinhsiang.fitracker.inbody.record

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.IN_BODY
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.USER
import com.pinhsiang.fitracker.data.InBody
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.ext.timestampToString
import com.pinhsiang.fitracker.user.UserManager

class InBodyRecordViewModel(private val selectedInBody: InBody) : ViewModel() {

    val selectedDate = selectedInBody.time.timestampToDate()

    val bodyWeightRecord = MutableLiveData<String>().apply {
        value = selectedInBody.bodyWeight.toString()
    }

    val bodyFatRecord = MutableLiveData<String>().apply {
        value = selectedInBody.bodyFat.toString()
    }

    val skeletalMuscleRecord = MutableLiveData<String>().apply {
        value = selectedInBody.skeletalMuscle.toString()
    }

    // Check that is all format of recorded numbers are correct.
    private val _validBodyWeight = MutableLiveData<Boolean>().apply {
        value = true
    }
    val validBodyWeight: LiveData<Boolean>
        get() = _validBodyWeight

    private val _validBodyFat = MutableLiveData<Boolean>().apply {
        value = true
    }
    val validBodyFat: LiveData<Boolean>
        get() = _validBodyFat

    private val _validSkeletalMuscle = MutableLiveData<Boolean>().apply {
        value = true
    }
    val validSkeletalMuscle: LiveData<Boolean>
        get() = _validSkeletalMuscle

    private val _dataUploading = MutableLiveData<Boolean>()
    val dataUploading: LiveData<Boolean>
        get() = _dataUploading

    private val _uploadDataDone = MutableLiveData<Boolean>()
    val uploadDataDone: LiveData<Boolean>
        get() = _uploadDataDone

    init {
        Log.i(TAG, "**********   InBodyRecordViewModel   *********")
        Log.i(TAG, "Selected InBody = $selectedInBody")
        Log.i(TAG, "Skeletal Muscle = ${selectedInBody.skeletalMuscle}%")
        Log.i(TAG, "Date = ${selectedInBody.time.timestampToString()}")
        Log.i(TAG, "**********   InBodyRecordViewModel   *********")
    }

    fun uploadData() {
        if (validBodyWeightFormat() && validBodyFatFormat() && validSkeletalMuscleFormat()) {

            if (bodyWeightRecord.value?.toFloat() == 0f || bodyFatRecord.value?.toFloat() == 0f || skeletalMuscleRecord.value?.toFloat() == 0f) {
                when {
                    bodyWeightRecord.value?.toFloat() == 0f -> _validBodyWeight.value = false
                    else -> _validBodyWeight.value = true
                }

                when {
                    bodyFatRecord.value?.toFloat() == 0f -> _validBodyFat.value = false
                    else -> _validBodyFat.value = true
                }

                when {
                    skeletalMuscleRecord.value?.toFloat() == 0f -> _validSkeletalMuscle.value = false
                    else -> _validSkeletalMuscle.value = true
                }

                Toast.makeText(
                    FitrackerApplication.appContext,
                    "Input values could not be zero.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                _validBodyWeight.value = true
                _validBodyFat.value = true
                _validSkeletalMuscle.value = true

                val inBodyToUpload = InBody(
                    time = selectedInBody.time,
                    bodyWeight = bodyWeightRecord.value?.toFloat()!!,
                    bodyFat = bodyFatRecord.value?.toFloat()!!,
                    skeletalMuscle = skeletalMuscleRecord.value?.toFloat()!!
                )

                _dataUploading.value = true

                FirebaseFirestore.getInstance()
                    .collection(USER)
                    .document(UserManager.userDocId!!)
                    .collection(IN_BODY)
                    .add(inBodyToUpload)
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

        } else {

            when {
                !validBodyWeightFormat() -> _validBodyWeight.value = false
                else -> _validBodyWeight.value = true
            }

            when {
                !validBodyFatFormat() -> _validBodyFat.value = false
                else -> _validBodyFat.value = true
            }

            when {
                !validSkeletalMuscleFormat() -> _validSkeletalMuscle.value = false
                else -> _validSkeletalMuscle.value = true
            }
        }
    }

    fun uploadCompletely() {
        _uploadDataDone.value = false
    }

    private fun validBodyWeightFormat(): Boolean {
        val recordedValueFormat = "[0-9]{1,3}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(bodyWeightRecord.value.toString())
    }

    private fun validBodyFatFormat(): Boolean {
        val recordedValueFormat = "[0-9]{1,2}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(bodyFatRecord.value.toString())
    }

    private fun validSkeletalMuscleFormat(): Boolean {
        val recordedValueFormat = "[0-9]{1,2}([.][0-9]{0,2})?".toRegex()
        return recordedValueFormat.matches(skeletalMuscleRecord.value.toString())
    }
}