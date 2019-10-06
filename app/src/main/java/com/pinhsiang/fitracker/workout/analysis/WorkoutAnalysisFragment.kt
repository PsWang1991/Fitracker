package com.pinhsiang.fitracker.workout.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.base.AnalysisBaseFragment
import com.pinhsiang.fitracker.databinding.FragmentWorkoutAnalysisBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.util.Util.getColor
import com.pinhsiang.fitracker.util.Util.getDrawable

class WorkoutAnalysisFragment : AnalysisBaseFragment() {

    private lateinit var binding: FragmentWorkoutAnalysisBinding
    override lateinit var chart: LineChart
    override lateinit var xAxis: XAxis
    override lateinit var yAxis: YAxis

    /**
     * Lazily initialize our [WorkoutAnalysisViewModel].
     */
    private val viewModel by viewModels<WorkoutAnalysisViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutAnalysisBinding.inflate(inflater, container, false)

        chart = binding.chartWorkout
        xAxis = chart.xAxis
        yAxis = chart.axisLeft

        setupLineChart()
        setupXAxis()
        setupYAxis()
        setupExerciseSpinner()
        setupGraphSpinner()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isDataReadyForPlotting.observe(this, Observer {
            if (it) {
                plotData(
                    x = viewModel.plottedDate,
                    y = viewModel.plottedValues
                )
                chart.invalidate()
                viewModel.plotDataDone()
            }
        })

        viewModel.periodFilter.observe(this, Observer { periodFilter ->
            periodFilter?.let {
                setAllPeriodBtnOff()
                setPeriodBtnOn(it)
            }
        })

        return binding.root
    }

    private fun setupExerciseSpinner() {

        val exerciseList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.exercise,
            R.layout.spinner_item
        )

        binding.spinnerExercise.adapter = exerciseList
        binding.spinnerExercise.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.setExerciseFilter(getString(R.string.bench_press))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.setExerciseFilter(getString(R.string.bench_press))

                    1 -> viewModel.setExerciseFilter(getString(R.string.deadlift))

                    2 -> viewModel.setExerciseFilter(getString(R.string.squat))
                }
            }
        }
    }

    private fun setupGraphSpinner() {

        val graphList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.graph_workout,
            R.layout.spinner_item
        )

        binding.spinnerGraph.adapter = graphList
        binding.spinnerGraph.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.setGraphFilter(WorkoutAnalysisViewModel.GRAPH_MAX_WEIGHT_PER_WORKOUT)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.setGraphFilter(WorkoutAnalysisViewModel.GRAPH_MAX_WEIGHT_PER_WORKOUT)

                    1 -> viewModel.setGraphFilter(WorkoutAnalysisViewModel.GRAPH_VOLUME_PER_WORKOUT)

                    2 -> viewModel.setGraphFilter(WorkoutAnalysisViewModel.GRAPH_SETS_PER_WORKOUT)

                    3 -> viewModel.setGraphFilter(WorkoutAnalysisViewModel.GRAPH_REPEATS_PER_WORKOUT)
                }
            }
        }
    }

    private fun setAllPeriodBtnOff() {

        binding.period1mWorkout.setTextColor(getColor(R.color.colorText))
        binding.period1mWorkout.background = getDrawable(R.drawable.btn_text_border)
        binding.period3mWorkout.setTextColor(getColor(R.color.colorText))
        binding.period3mWorkout.background = getDrawable(R.drawable.btn_text_border)
        binding.period6mWorkout.setTextColor(getColor(R.color.colorText))
        binding.period6mWorkout.background = getDrawable(R.drawable.btn_text_border)
        binding.period1yWorkout.setTextColor(getColor(R.color.colorText))
        binding.period1yWorkout.background = getDrawable(R.drawable.btn_text_border)
        binding.periodAllWorkout.setTextColor(getColor(R.color.colorText))
        binding.periodAllWorkout.background = getDrawable(R.drawable.btn_text_border)
    }

    private fun setPeriodBtnOn(period: Long) {

        when (period) {

            PERIOD_3M -> {
                binding.period3mWorkout.setTextColor(getColor(R.color.colorBackground))
                binding.period3mWorkout.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            PERIOD_6M -> {
                binding.period6mWorkout.setTextColor(getColor(R.color.colorBackground))
                binding.period6mWorkout.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            PERIOD_1Y -> {
                binding.period1yWorkout.setTextColor(getColor(R.color.colorBackground))
                binding.period1yWorkout.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            viewModel.currentTime -> {
                binding.periodAllWorkout.setTextColor(getColor(R.color.colorBackground))
                binding.periodAllWorkout.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            else -> {
                binding.period1mWorkout.setTextColor(getColor(R.color.colorBackground))
                binding.period1mWorkout.background = getDrawable(R.drawable.btn_text_border_inverse)
            }
        }
    }
}