package com.pinhsiang.fitracker.inbody

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.InBody
import com.pinhsiang.fitracker.ext.digits
import com.pinhsiang.fitracker.user.UserManager
import org.threeten.bp.LocalDate
import java.sql.Timestamp

class InBodyViewModel : ViewModel() {

    private val allInBodyData = mutableListOf<InBody>()
    val displayBodyWeight = MutableLiveData<String>()
    val displayBodyFat = MutableLiveData<String>()
    val displaySkeletalMuscle = MutableLiveData<String>()

    var selectedDate: LocalDate? = null
    val today: LocalDate = LocalDate.now()

    var calendarExpanding = MutableLiveData<Boolean>().apply {
        value = true
    }

    // Handle navigation to motion fragment.
    private val _navigationToRecord = MutableLiveData<Boolean>()
    val navigationToRecord: LiveData<Boolean>
        get() = _navigationToRecord

    val downloadComplete = MutableLiveData<Boolean>().apply {
        value = false
    }

    init {
        downloadWorkoutData()
    }

    fun addNewData() {
        _navigationToRecord.value = true
    }

    fun addNewDataDone() {
        _navigationToRecord.value = false
    }

    // This function get the last in-body data on selected date.
    fun refreshInBodyDataByDate(date: LocalDate) {

        if (hasInBodyData(date)) {

            val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
            val lastInBodyData = allInBodyData.filter {
                it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
            }.maxBy { it.time }!!

            displayBodyFat.value = lastInBodyData.bodyFat.digits(2)
            displayBodyWeight.value = lastInBodyData.bodyWeight.digits(2)
            displaySkeletalMuscle.value = lastInBodyData.skeletalMuscle.digits(2)

        } else {

            displayBodyFat.value = null
            displayBodyWeight.value = null
            displaySkeletalMuscle.value = null
        }
    }

    fun hasInBodyData(date: LocalDate): Boolean {

        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allInBodyData.any {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
    }

    private fun downloadWorkoutData() {

        FirebaseFirestore.getInstance()
            .collection(USER)
            .document(UserManager.userDocId!!)
            .collection(IN_BODY)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val workout = document.toObject(InBody::class.java)
                    workout.id = document.id
                    allInBodyData.add(workout)
                }
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {

                Log.i(TAG, "allInBodyData = $allInBodyData")
                refreshInBodyDataByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }

    fun setCalendarExpandingTrue() {
        calendarExpanding.value = true
    }

    fun setCalendarExpandingFalse() {
        calendarExpanding.value = false
    }
}