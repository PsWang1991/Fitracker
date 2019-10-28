package com.pinhsiang.fitracker.workout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.user.UserManager
import org.threeten.bp.LocalDate
import java.sql.Timestamp

class WorkoutViewModel : ViewModel() {

    private val allWorkoutData = mutableListOf<Workout>()

    private val _workoutList = MutableLiveData<List<Workout>>()
    val workoutList: LiveData<List<Workout>>
        get() = _workoutList

    private val _hasData = MutableLiveData<Boolean>().apply {
        value = false
    }
    val hasData: LiveData<Boolean>
        get() = _hasData

    var selectedDate: LocalDate? = null
    val today: LocalDate = LocalDate.now()

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
        Log.i(TAG, "UserManager.userDocId = ${UserManager.userDocId}")
    }

    fun addNewData() {
        _navigationToMotion.value = true
    }

    fun navigateToMotionDone() {
        _navigationToMotion.value = false
    }

    fun refreshWorkoutListByDate(date: LocalDate) {

        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        _workoutList.value = allWorkoutData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }

        _hasData.value = _workoutList.value!!.isNotEmpty()
    }

    fun hasWorkoutData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allWorkoutData.any {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
    }

    private fun downloadWorkoutData() {

        FirebaseFirestore.getInstance()
            .collection(USER)
            .document(UserManager.userDocId!!)
            .collection(WORKOUT)
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
            }
            .addOnCompleteListener {

                Log.i(TAG, "allWorkoutData = $allWorkoutData")
                refreshWorkoutListByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }
}