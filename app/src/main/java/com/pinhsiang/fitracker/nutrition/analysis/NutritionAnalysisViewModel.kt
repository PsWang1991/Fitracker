package com.pinhsiang.fitracker.nutrition.analysis

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.user.UserManager

class NutritionAnalysisViewModel : ViewModel() {

    val currentTime = System.currentTimeMillis()

    private var selectedNutrient = TOTAL_DAILY_ENERGY_EXTRACTED

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = PERIOD_1M
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val rawNutritionData = mutableListOf<Nutrition>()
    private val dailyTotalNutritionData = mutableListOf<Nutrition>()

    var plottedValues = mutableListOf<Entry>()

    var plottedDate = mutableListOf<String>()

    private val _isDataReadyForPlotting = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isDataReadyForPlotting: LiveData<Boolean>
        get() = _isDataReadyForPlotting


    init {
        downloadNutritionData()
    }

    private fun downloadNutritionData() {
        FirebaseFirestore.getInstance()
            .collection(USER)
            .document(UserManager.userDocId!!)
            .collection(NUTRITION)
            .orderBy("time")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nutrition = document.toObject(Nutrition::class.java)
                    rawNutritionData.add(nutrition)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
            .addOnCompleteListener {
                rawDataToDailyData()
                refreshPlottedData()
                _isDataReadyForPlotting.value = true
            }
    }

    /**
     * We show nutrition data by date, but by each meal.
     * Notice : raw data should be sorted by time before transforms it to daily data.
     **/
    private fun rawDataToDailyData() {
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

    private fun refreshPlottedData() {
        plottedDate.clear()
        plottedValues.clear()
        val filteredData = dailyTotalNutritionData.filter {
            it.time <= currentTime &&
                    it.time >= currentTime - _periodFilter.value!!
        }
        when (selectedNutrient) {
            TOTAL_DAILY_ENERGY_EXTRACTED -> {
                filteredData.forEachIndexed { index, nutrition ->
                    plottedValues.add(
                        Entry(
                            index.toFloat(),
                            (nutrition.carbohydrate * 4 + nutrition.protein * 4 + nutrition.fat * 9).toFloat()
                        )
                    )
                    plottedDate.add(nutrition.time.timestampToDate())
                }
            }
            PROTEIN -> {
                filteredData.forEachIndexed { index, nutrition ->
                    plottedValues.add(Entry(index.toFloat(), nutrition.protein.toFloat()))
                    plottedDate.add(nutrition.time.timestampToDate())
                }
            }
            CARBOHYDRATE -> {
                filteredData.forEachIndexed { index, nutrition ->
                    plottedValues.add(Entry(index.toFloat(), nutrition.carbohydrate.toFloat()))
                    plottedDate.add(nutrition.time.timestampToDate())
                }
            }
            FAT -> {
                filteredData.forEachIndexed { index, nutrition ->
                    plottedValues.add(Entry(index.toFloat(), nutrition.fat.toFloat()))
                    plottedDate.add(nutrition.time.timestampToDate())
                }
            }
        }
    }

    fun setNutrient(nutrient: Int) {
        selectedNutrient = nutrient
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun selectPeriod(periodFilterBtn: View) {
        _periodFilter.value = when (periodFilterBtn.tag) {
            TAG_1M -> PERIOD_1M
            TAG_3M -> PERIOD_3M
            TAG_6M -> PERIOD_6M
            TAG_1Y -> PERIOD_1Y
            else -> currentTime
        }
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun plotDataDone() {
        _isDataReadyForPlotting.value = false
    }

    companion object {
        const val TOTAL_DAILY_ENERGY_EXTRACTED = 0
        const val PROTEIN = 1
        const val CARBOHYDRATE = 2
        const val FAT = 3
    }

}
