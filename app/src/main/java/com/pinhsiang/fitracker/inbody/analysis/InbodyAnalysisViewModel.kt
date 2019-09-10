package com.pinhsiang.fitracker.inbody.analysis

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.util.Util.getString


const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"
const val MILLISECOND_PER_DAY = 86400000L
const val DAYS_PER_1M = 32L
const val DAYS_PER_3M = 93L
const val DAYS_PER_6M = 186L
const val DAYS_PER_1Y = 366L

class InbodyAnalysisViewModel(app: Application) : AndroidViewModel(app) {

    val db = FirebaseFirestore.getInstance()

    val currentTime = System.currentTimeMillis()

    private var selectedInbodyFilter = getString(R.string.body_weight_inbody)

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = DAYS_PER_1M * MILLISECOND_PER_DAY
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val allInbodyData = mutableListOf<Inbody>()

    var valuesToPLot = mutableListOf<Entry>()

    var xAxisDateToPlot = mutableListOf<String>()

    private val _plotDataReady = MutableLiveData<Boolean>().apply {
        value = false
    }
    val plotDataReady: LiveData<Boolean>
        get() = _plotDataReady

    init {
        downloadInbodyData()
    }

    private fun downloadInbodyData() {
        db.collection("user").document(USER_DOC_NAME)
            .collection("in-body")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val inbody = document.toObject(Inbody::class.java)
                    allInbodyData.add(inbody)
//                    Log.i(TAG, "workout = $workout")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }.addOnCompleteListener {
                //                Log.w(TAG, "allWorkoutData = $allWorkoutData")
//                Log.w(TAG, "allWorkoutData size = ${allWorkoutData.size}")
                setDataToPlot()
                _plotDataReady.value = true
            }
    }

    private fun setDataToPlot() {
        xAxisDateToPlot.clear()
        valuesToPLot.clear()
        val filteredData = allInbodyData.filter {
            it.time <= currentTime &&
                    it.time >= currentTime - _periodFilter.value!!
        }.sortedBy { it.time }
        when (selectedInbodyFilter) {
            getString(R.string.body_weight_inbody) -> {
                filteredData.forEachIndexed { index, inbody ->
                    valuesToPLot.add(Entry(index.toFloat(), inbody.bodyWeight))
                    xAxisDateToPlot.add(inbody.time.timestampToDate())
                }
            }
            getString(R.string.skeletal_muscle_inbody) -> {
                filteredData.forEachIndexed { index, inbody ->
                    valuesToPLot.add(Entry(index.toFloat(), inbody.skeletalMuscle))
                    xAxisDateToPlot.add(inbody.time.timestampToDate())
                }
            }
            getString(R.string.body_fat_inbody) -> {
                filteredData.forEachIndexed { index, inbody ->
                    valuesToPLot.add(Entry(index.toFloat(), inbody.bodyFat))
                    xAxisDateToPlot.add(inbody.time.timestampToDate())
                }
            }
        }
    }

    fun setInbodyFilter(inbodyFilter: String) {
        selectedInbodyFilter = inbodyFilter
        setDataToPlot()
        _plotDataReady.value = true
    }

    fun selectPeriod1M() {
        _periodFilter.value = DAYS_PER_1M * MILLISECOND_PER_DAY
        setDataToPlot()
        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod3M() {
        _periodFilter.value = DAYS_PER_3M * MILLISECOND_PER_DAY
        setDataToPlot()
        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod6M() {
        _periodFilter.value = DAYS_PER_6M * MILLISECOND_PER_DAY
        setDataToPlot()
        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod1Y() {
        _periodFilter.value = DAYS_PER_1Y * MILLISECOND_PER_DAY
        setDataToPlot()
        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriodAll() {
        _periodFilter.value = currentTime
        setDataToPlot()
        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun plotDataDone() {
        _plotDataReady.value = false
    }
}

