package com.pinhsiang.fitracker.inbody.analysis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.InBody
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.user.UserManager

const val FILTER_BODY_WEIGHT = 0
const val FILTER_SKELETAL_MUSCLE = 1
const val FILTER_BODY_FAT = 2

class InBodyAnalysisViewModel : ViewModel() {

    val currentTime = System.currentTimeMillis()

    private var selectedInBodyFilter = FILTER_BODY_WEIGHT

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = DAYS_PER_1M * MILLISECOND_PER_DAY
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val allInBodyData = mutableListOf<InBody>()

    var valuesToBePlotted = mutableListOf<Entry>()

    var xAxisDateToBePlotted = mutableListOf<String>()

    private val _isDataReadyForPlotting = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isDataReadyForPlotting: LiveData<Boolean>
        get() = _isDataReadyForPlotting

    init {
        downloadInBodyData()
    }

    private fun downloadInBodyData() {
        FirebaseFirestore.getInstance()
            .collection(USER)
            .document(UserManager.userDocId!!)
            .collection(IN_BODY)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val inBody = document.toObject(InBody::class.java)
                    allInBodyData.add(inBody)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                refreshPlottedData()
                _isDataReadyForPlotting.value = true
            }
    }

    private fun refreshPlottedData() {
        xAxisDateToBePlotted.clear()
        valuesToBePlotted.clear()
        val filteredInBodyData = allInBodyData.filter {
            it.time <= currentTime && it.time >= currentTime - _periodFilter.value!!
        }.sortedBy { it.time }

        when (selectedInBodyFilter) {
            FILTER_BODY_WEIGHT -> {
                filteredInBodyData.forEachIndexed { index, inBody ->
                    valuesToBePlotted.add(Entry(index.toFloat(), inBody.bodyWeight))
                    xAxisDateToBePlotted.add(inBody.time.timestampToDate())
                }
            }
            FILTER_SKELETAL_MUSCLE -> {
                filteredInBodyData.forEachIndexed { index, inBody ->
                    valuesToBePlotted.add(Entry(index.toFloat(), inBody.skeletalMuscle))
                    xAxisDateToBePlotted.add(inBody.time.timestampToDate())
                }
            }
            FILTER_BODY_FAT -> {
                filteredInBodyData.forEachIndexed { index, inBody ->
                    valuesToBePlotted.add(Entry(index.toFloat(), inBody.bodyFat))
                    xAxisDateToBePlotted.add(inBody.time.timestampToDate())
                }
            }
        }
    }

    fun setInBodyFilter(inBodyFilter: Int) {
        selectedInBodyFilter = inBodyFilter
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriod1M() {
        _periodFilter.value = DAYS_PER_1M * MILLISECOND_PER_DAY
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriod3M() {
        _periodFilter.value = DAYS_PER_3M * MILLISECOND_PER_DAY
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriod6M() {
        _periodFilter.value = DAYS_PER_6M * MILLISECOND_PER_DAY
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriod1Y() {
        _periodFilter.value = DAYS_PER_1Y * MILLISECOND_PER_DAY
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriodAll() {
        _periodFilter.value = currentTime
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun plotDataDone() {
        _isDataReadyForPlotting.value = false
    }
}

