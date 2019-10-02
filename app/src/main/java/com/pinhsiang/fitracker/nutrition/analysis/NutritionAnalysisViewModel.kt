package com.pinhsiang.fitracker.nutrition.analysis

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.Util.getString

class NutritionAnalysisViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()

    val currentTime = System.currentTimeMillis()

    private var selectedNutrient = getString(R.string.total_daily_energy_extracted)

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = DAYS_PER_1M * MILLISECOND_PER_DAY
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val rawNutritionData = mutableListOf<Nutrition>()
    private val dailyTotalNutritionData = mutableListOf<Nutrition>()

    var valuesToPLot = mutableListOf<Entry>()

    var xAxisDateToPlot = mutableListOf<String>()

    private val _plotDataReady = MutableLiveData<Boolean>().apply {
        value = false
    }
    val plotDataReady: LiveData<Boolean>
        get() = _plotDataReady


    init {
        downloadNutritionData()
    }

    private fun downloadNutritionData() {
        db.collection(USER).document(UserManager.userDocId!!)
            .collection(NUTRITION).orderBy("time")
            .get()
            .addOnSuccessListener { result ->
                Log.i(TAG, "****** From Firebase ********")
                for (document in result) {
                    val nutrition = document.toObject(Nutrition::class.java)
                    rawNutritionData.add(nutrition)
                    Log.i(TAG, "nutrition = $nutrition")
                }
                Log.i(TAG, "****** From Firebase ********")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                rawToDailyTotal()
                setDataToPlot()
                _plotDataReady.value = true
            }
    }

    // We show nutrition data by date, but by meal.
    // Notice : raw data should be sorted by time.
    private fun rawToDailyTotal() {
        if (rawNutritionData.isNotEmpty()) {
            var currentDate = rawNutritionData[0].time.timestampToDate()
            dailyTotalNutritionData.add(rawNutritionData[0])
            rawNutritionData.removeAt(0)
            while (rawNutritionData.isNotEmpty()) {
                if (rawNutritionData[0].time.timestampToDate() == currentDate) {
                    dailyTotalNutritionData.last().protein += rawNutritionData[0].protein
                    dailyTotalNutritionData.last().carbohydrate += rawNutritionData[0].carbohydrate
                    dailyTotalNutritionData.last().fat += rawNutritionData[0].fat
                    rawNutritionData.removeAt(0)
                } else {
                    currentDate = rawNutritionData[0].time.timestampToDate()
                    dailyTotalNutritionData.add(rawNutritionData[0])
                    rawNutritionData.removeAt(0)
                }
            }
        }
    }

    private fun setDataToPlot() {
        xAxisDateToPlot.clear()
        valuesToPLot.clear()
        val filteredData = dailyTotalNutritionData.filter {
            it.time <= currentTime &&
                    it.time >= currentTime - _periodFilter.value!!
        }
        when (selectedNutrient) {
            getString(R.string.total_daily_energy_extracted) -> {
                filteredData.forEachIndexed { index, nutrition ->
                    valuesToPLot.add(
                        Entry(
                            index.toFloat(),
                            (nutrition.carbohydrate * 4 + nutrition.protein * 4 + nutrition.fat * 9).toFloat()
                        )
                    )
                    xAxisDateToPlot.add(nutrition.time.timestampToDate())
                }
            }
            getString(R.string.protein) -> {
                filteredData.forEachIndexed { index, nutrition ->
                    valuesToPLot.add(Entry(index.toFloat(), nutrition.protein.toFloat()))
                    xAxisDateToPlot.add(nutrition.time.timestampToDate())
                }
            }
            getString(R.string.carbohydrate) -> {
                filteredData.forEachIndexed { index, nutrition ->
                    valuesToPLot.add(Entry(index.toFloat(), nutrition.carbohydrate.toFloat()))
                    xAxisDateToPlot.add(nutrition.time.timestampToDate())
                }
            }
            getString(R.string.fat) -> {
                filteredData.forEachIndexed { index, nutrition ->
                    valuesToPLot.add(Entry(index.toFloat(), nutrition.fat.toFloat()))
                    xAxisDateToPlot.add(nutrition.time.timestampToDate())
                }
            }
        }
    }

    fun setNutrient(nutrient: String) {
        selectedNutrient = nutrient
        setDataToPlot()
        _plotDataReady.value = true
    }

    fun selectPeriod1M() {
        _periodFilter.value = DAYS_PER_1M * MILLISECOND_PER_DAY
//        setDataToPlot()
//        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod3M() {
        _periodFilter.value = DAYS_PER_3M * MILLISECOND_PER_DAY
//        setDataToPlot()
//        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod6M() {
        _periodFilter.value = DAYS_PER_6M * MILLISECOND_PER_DAY
//        setDataToPlot()
//        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriod1Y() {
        _periodFilter.value = DAYS_PER_1Y * MILLISECOND_PER_DAY
//        setDataToPlot()
//        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun selectPeriodAll() {
        _periodFilter.value = currentTime
//        setDataToPlot()
//        _plotDataReady.value = true
//        Log.i(TAG, "_periodFilter = $_periodFilter")
    }

    fun plotDataDone() {
        _plotDataReady.value = false
    }
}
