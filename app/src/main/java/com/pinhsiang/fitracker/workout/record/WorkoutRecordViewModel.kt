package com.pinhsiang.fitracker.workout.record

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"

class WorkoutRecordViewModel(val selectedWorkout: Workout, val app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    val selectedDate = selectedWorkout.time.timestampToDate()

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

    fun addDataToFirebase() {
        if (setListTemp.isNotEmpty()) {
            var workoutToAdded = Workout(
                time = selectedWorkout.time,
                motion = selectedWorkout.motion,
                maxWeight = setListTemp.maxBy { it.liftWeight }!!.liftWeight,
                sets = setListTemp
            )

            db.collection("user").document(USER_DOC_NAME)
                .collection("workout").add(workoutToAdded)
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
                .addOnCompleteListener {
                    Toast.makeText(app, "Data saving completed.", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(app, "Set data is empty.", Toast.LENGTH_SHORT).show()
        }
    }
}