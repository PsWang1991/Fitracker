package com.pinhsiang.fitracker.inbody

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.Util
import com.pinhsiang.fitracker.util.Util.getString
import org.threeten.bp.LocalDate
import java.sql.Timestamp

const val MILLISECOND_PER_DAY = 86400000L
const val ZERO_HOUR = "00:00:00"

class InbodyViewModel(app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    private val allInbodyData = mutableListOf<Inbody>()
    val displayBodyWeight = MutableLiveData<String>()
    val displayBodyFat = MutableLiveData<String>()
    val displaySkeletalMuscle = MutableLiveData<String>()


    var selectedDate: LocalDate? = null
    val today = LocalDate.now()

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

    // This function get the last data on selected date.
    fun getInbodyDataByDate(date: LocalDate) {
        if (hasInbodyData(date)) {
            val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
            val lastInbodyData = allInbodyData.filter {
                it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
            }.maxBy { it.time }!!
            displayBodyFat.value = lastInbodyData.bodyFat.toString()
            displayBodyWeight.value = lastInbodyData.bodyWeight.toString()
            displaySkeletalMuscle.value = lastInbodyData.skeletalMuscle.toString()
            Log.i(TAG, "lastInbodyData = $lastInbodyData")
        } else {
            displayBodyFat.value = null
            displayBodyWeight.value = null
            displaySkeletalMuscle.value = null
            Log.i(TAG, "No In-body Data!!!")
        }
        Log.i(TAG, "allInbodyData = $allInbodyData")


    }

    fun hasInbodyData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allInbodyData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    private fun downloadWorkoutData() {
        db.collection(getString(R.string.user_collection_path)).document(UserManager.userDocId!!)
            .collection(getString(R.string.inbody_collection_path))
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Inbody::class.java)
                    workout.id = document.id
                    allInbodyData.add(workout)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }.addOnCompleteListener {
                Log.i(TAG, "allInbodyData = $allInbodyData")
                getInbodyDataByDate(LocalDate.now())
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