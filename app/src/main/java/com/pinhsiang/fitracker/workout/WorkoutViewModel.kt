package com.pinhsiang.fitracker.workout

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.Workout
import org.threeten.bp.LocalDate
import java.sql.Timestamp

const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"
const val MILLISECOND_PER_DAY = 86400000L
const val MILLISECOND_PER_WEEK = MILLISECOND_PER_DAY * 7
const val MILLISECOND_PER_MONTH = MILLISECOND_PER_DAY * 30
const val ZERO_HOUR = "00:00:00"

class WorkoutViewModel(app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    private val allWorkoutData = mutableListOf<Workout>()

    private val _workoutList = MutableLiveData<List<Workout>>()
    val workoutList: LiveData<List<Workout>>
        get() = _workoutList

    var selectedDate: LocalDate? = null
    val today = LocalDate.now()

    var calendarExpanding = true

    // Handle navigation to motion fragment.
    private val _navigationToMotion = MutableLiveData<Boolean>()
    val navigationToMotion: LiveData<Boolean>
        get() = _navigationToMotion

    val downloadComplete = MutableLiveData<Boolean>().apply {
        value = false
    }

    init {
        downloadWorkoutData()
    }

    fun addNewData() {
        _navigationToMotion.value = true
    }

    fun addNewDataDone() {
        _navigationToMotion.value = false
    }

    fun getWorkoutDataByDate(date: LocalDate) {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        _workoutList.value = allWorkoutData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
//        Log.i(TAG, "allWorkoutData = $allWorkoutData")
//        Log.i(TAG, "workoutList = ${_workoutList.value}")
    }

    fun hasWorkoutData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allWorkoutData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    private fun downloadWorkoutData() {
        db.collection("user").document(USER_DOC_NAME)
            .collection("workout")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Workout::class.java)
                    workout.id = document.id
                    allWorkoutData.add(workout)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }.addOnCompleteListener {
                Log.i(TAG, "allWorkoutData = $allWorkoutData")
                getWorkoutDataByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }
}