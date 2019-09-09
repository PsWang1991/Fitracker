package com.pinhsiang.fitracker.workout.analysis

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.util.Util.getString


const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"
const val MILLISECOND_PER_DAY = 86400000L
const val DAYS_PER_1M = 32L
const val DAYS_PER_3M = 93L
const val DAYS_PER_6M = 186L
const val DAYS_PER_1Y = 366L

class WorkoutAnalysisViewModel(app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    val currentTime = System.currentTimeMillis()

    private var selectedExercise = getString(R.string.bench_press)

    private var selectedGraph = getString(R.string.max_weight_workout)

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = DAYS_PER_1M * MILLISECOND_PER_DAY
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val allWorkoutData = mutableListOf<Workout>()

    var valuesToPLot = mutableListOf<Entry>()

    var xAxisDateToPlot = mutableListOf<String>()

    private val _plotDataReady = MutableLiveData<Boolean>().apply {
        value = false
    }
    val plotDataReady: LiveData<Boolean>
        get() = _plotDataReady

    init {
        downloadWorkoutData()
    }

    private fun downloadWorkoutData() {
        db.collection("user").document(USER_DOC_NAME)
            .collection("workout")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Workout::class.java)
                    allWorkoutData.add(workout)
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
        val filteredData = allWorkoutData.filter {
            it.motion == selectedExercise &&
                    it.time <= currentTime &&
                    it.time >= currentTime - _periodFilter.value!!
        }.sortedBy { it.time }
        when(selectedGraph) {
            getString(R.string.max_weight_workout) -> {
                filteredData.forEachIndexed { index, workout ->
                    valuesToPLot.add(Entry(index.toFloat(), workout.maxWeight.toFloat()))
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
            getString(R.string.volume_workout) -> {
                filteredData.forEachIndexed { index, workout ->
                    var volume = 0
                    if (workout.sets != null && workout.sets.isNotEmpty()) {
                        for (set in workout.sets!!) {
                            volume += set.repeats * set.liftWeight
                        }
                    }
                    valuesToPLot.add(Entry(index.toFloat(), volume.toFloat()))
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
            getString(R.string.sets_workout) -> {
                filteredData.forEachIndexed { index, workout ->
                    if (workout.sets != null && workout.sets.isNotEmpty()) {
                        valuesToPLot.add(Entry(index.toFloat(), workout.sets!!.size.toFloat()))
                    } else {
                        valuesToPLot.add(Entry(index.toFloat(), 0f))
                    }
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
            getString(R.string.repeats_workout) -> {
                filteredData.forEachIndexed { index, workout ->
                    var repeats = 0
                    if (workout.sets != null && workout.sets.isNotEmpty()) {
                        for (set in workout.sets!!) {
                            repeats += set.repeats
                        }
                    }
                    valuesToPLot.add(Entry(index.toFloat(), repeats.toFloat()))
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
        }

    }

    fun setExerciseFilter(motion: String) {
        selectedExercise = motion
        setDataToPlot()
        _plotDataReady.value = true
    }

    fun setGraphFilter(graph: String) {
        selectedGraph = graph
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

