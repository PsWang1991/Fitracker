package com.pinhsiang.fitracker.inbody

import android.app.Application
import android.util.Log
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import org.threeten.bp.LocalDate
import java.sql.Timestamp
import java.util.*

const val TAG = "Fitracker"
const val MILLISECOND_PER_DAY = 86400000L
const val MILLISECOND_PER_WEEK = MILLISECOND_PER_DAY * 7
const val MILLISECOND_PER_MONTH = MILLISECOND_PER_DAY * 30
const val ZERO_HOUR = "00:00:00"

class InbodyViewModel(app: Application) : AndroidViewModel(app) {

    // Internal and external in-body data list
    private val _inbodyList = mutableListOf<Inbody>()
    //    val workoutList = MutableLiveData<List<Workout>>()
    private val inbodyList = MutableLiveData<List<Inbody>>()
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

    init {
        createMockInbodyData()
        getInbodyDataByDate(LocalDate.now())
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
            val lastInbodyData = _inbodyList.filter {
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
        Log.i(TAG, "_inbodyList = $_inbodyList")


    }

    fun hasInbodyData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return _inbodyList.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    fun setCalendarExpandingTrue() {
        calendarExpanding.value = true
    }

    fun setCalendarExpandingFalse() {
        calendarExpanding.value = false
    }

    private fun createMockInbodyData() {
        val inbody1 = Inbody(bodyWeight = 53.0F, bodyFat = 12.2F, skeletalMuscle = 31.4F)
        val inbody2 = Inbody(
            time = System.currentTimeMillis() - MILLISECOND_PER_DAY,
            bodyWeight = 52.8F,
            bodyFat = 13.5F,
            skeletalMuscle = 30.9F
        )
        val inbody3 = Inbody(
            time = System.currentTimeMillis() - 2 * MILLISECOND_PER_DAY,
            bodyWeight = 52.1F,
            bodyFat = 12.8F,
            skeletalMuscle = 32.1F
        )
        val inbody4 = Inbody(
            time = System.currentTimeMillis() - 3 * MILLISECOND_PER_DAY,
            bodyWeight = 53.7F,
            bodyFat = 13.7F,
            skeletalMuscle = 30.7F
        )
        val inbody5 = Inbody(
            time = System.currentTimeMillis() - 4 * MILLISECOND_PER_DAY,
            bodyWeight = 54.3F,
            bodyFat = 13.4F,
            skeletalMuscle = 31.2F
        )
        val inbody6 = Inbody(
            time = System.currentTimeMillis() - 5 * MILLISECOND_PER_DAY,
            bodyWeight = 54.9F,
            bodyFat = 12.0F,
            skeletalMuscle = 36.2F
        )
        val inbody7 = Inbody(
            time = System.currentTimeMillis() - 6 * MILLISECOND_PER_DAY,
            bodyWeight = 55.7F,
            bodyFat = 11.8F,
            skeletalMuscle = 36.3F
        )
        val inbody8 = Inbody(
            time = System.currentTimeMillis() - 7 * MILLISECOND_PER_DAY,
            bodyWeight = 54.9F,
            bodyFat = 12.2F,
            skeletalMuscle = 36.1F
        )
        val inbody9 = Inbody(
            time = System.currentTimeMillis() - 8 * MILLISECOND_PER_DAY,
            bodyWeight = 54.7F,
            bodyFat = 13.3F,
            skeletalMuscle = 35.7F
        )
        val inbody10 = Inbody(
            time = System.currentTimeMillis() - 9 * MILLISECOND_PER_DAY,
            bodyWeight = 55.9F,
            bodyFat = 13.9F,
            skeletalMuscle = 35.5F
        )

        val dataList = listOf(inbody1, inbody2, inbody3, inbody4, inbody5, inbody6, inbody7, inbody8, inbody9, inbody10)
        _inbodyList.addAll(dataList)
    }
}