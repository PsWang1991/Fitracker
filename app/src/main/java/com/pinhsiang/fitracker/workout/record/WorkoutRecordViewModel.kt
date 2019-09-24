package com.pinhsiang.fitracker.workout.record

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.timestampToString
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.Util.getString

class WorkoutRecordViewModel(val selectedWorkout: Workout, val app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    val selectedDate = selectedWorkout.time.timestampToDate()

    // Internal and external set list
    private val setListTemp = mutableListOf<Sets>()

    private val _setList = MutableLiveData<List<Sets>>()
    val setList: LiveData<List<Sets>>
        get() = _setList

    private val _addToSetList = MutableLiveData<Boolean>()
    val addToSetList: LiveData<Boolean>
        get() = _addToSetList

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
        if (weightRecord.value!! == 0 || repeatsRecord.value!! == 0) {
            Toast.makeText(app, "Weight and repeats could not be zero.", Toast.LENGTH_SHORT).show()
        } else {
            setListTemp.add(Sets(weightRecord.value!!, repeatsRecord.value!!))
            _setList.value = setListTemp
            _addToSetList.value = true
        }
        Log.i(TAG, "weightRecord = ${weightRecord.value}")
        Log.i(TAG, "repeatsRecord = ${repeatsRecord.value}")
//        Log.i(TAG, "${setList.value}")
    }

    fun setAddToSetListFalse() {
        _addToSetList.value = false
    }

    fun endOfSetList(): Int {
        return setListTemp.size - 1
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

    fun reviseModeOff() {
        revisedDataIndex = -1
        reviseMode.value = false
    }

    fun deleteSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp.removeAt(revisedDataIndex)
            _setList.value = setListTemp
            reviseModeOff()
        }
    }

    fun updateSelectedData() {
        if (revisedDataIndex in 0 until setListTemp.size) {
            setListTemp[revisedDataIndex] = Sets(liftWeight = weightRecord.value!!, repeats = repeatsRecord.value!!)
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

            db.collection(getString(R.string.user_collection_path)).document(UserManager.userDocId!!)
                .collection(getString(R.string.workout_collection_path)).add(workoutToAdded)
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