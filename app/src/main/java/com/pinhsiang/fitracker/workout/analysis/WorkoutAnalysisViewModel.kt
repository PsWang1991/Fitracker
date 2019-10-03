package com.pinhsiang.fitracker.workout.analysis

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.Util.getString

class WorkoutAnalysisViewModel : ViewModel() {

    val currentTime = System.currentTimeMillis()

    private var selectedExercise = getString(R.string.bench_press)

    private var selectedGraph = GRAPH_MAX_WEIGHT_PER_WORKOUT

    private val _periodFilter = MutableLiveData<Long>().apply {
        value = DAYS_PER_1M * MILLISECOND_PER_DAY
    }
    val periodFilter: LiveData<Long>
        get() = _periodFilter

    private val allWorkoutData = mutableListOf<Workout>()

    var valuesToPLot = mutableListOf<Entry>()

    var xAxisDateToPlot = mutableListOf<String>()

    private val _isDataReadyForPlotting = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isDataReadyForPlotting: LiveData<Boolean>
        get() = _isDataReadyForPlotting

    init {
        downloadWorkoutData()
    }

    private fun downloadWorkoutData() {
        FirebaseFirestore.getInstance().collection(USER).document(UserManager.userDocId!!)
            .collection(WORKOUT)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Workout::class.java)
                    allWorkoutData.add(workout)
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
        xAxisDateToPlot.clear()
        valuesToPLot.clear()
        val filteredData =
            allWorkoutData.filter {
                it.motion == selectedExercise &&
                        it.time <= currentTime &&
                        it.time >= currentTime - _periodFilter.value!!
            }.sortedBy { it.time }

        when (selectedGraph) {
            GRAPH_MAX_WEIGHT_PER_WORKOUT -> {
                filteredData.forEachIndexed { index, workout ->
                    valuesToPLot.add(Entry(index.toFloat(), workout.maxWeight.toFloat()))
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
            GRAPH_VOLUME_PER_WORKOUT -> {
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
            GRAPH_SETS_PER_WORKOUT -> {
                filteredData.forEachIndexed { index, workout ->
                    if (workout.sets != null && workout.sets.isNotEmpty()) {
                        valuesToPLot.add(Entry(index.toFloat(), workout.sets.size.toFloat()))
                    } else {
                        valuesToPLot.add(Entry(index.toFloat(), 0f))
                    }
                    xAxisDateToPlot.add(workout.time.timestampToDate())
                }
            }
            GRAPH_REPEATS_PER_WORKOUT -> {
                filteredData.forEachIndexed { index, workout ->
                    var repeats = 0
                    if (workout.sets != null && workout.sets.isNotEmpty()) {
                        for (set in workout.sets) {
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
        refreshPlottedData()
        _isDataReadyForPlotting.value = true
    }

    fun setGraphFilter(graphType: Int) {
        selectedGraph = graphType
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
        const val GRAPH_MAX_WEIGHT_PER_WORKOUT = 0
        const val GRAPH_VOLUME_PER_WORKOUT = 1
        const val GRAPH_SETS_PER_WORKOUT = 2
        const val GRAPH_REPEATS_PER_WORKOUT = 3
    }

}