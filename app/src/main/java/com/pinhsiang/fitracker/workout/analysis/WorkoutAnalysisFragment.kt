package com.pinhsiang.fitracker.workout.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.databinding.FragmentWorkoutAnalysisBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.util.Util
import com.pinhsiang.fitracker.util.Util.getColor
import com.pinhsiang.fitracker.util.Util.getDrawable
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel.Companion.GRAPH_MAX_WEIGHT_PER_WORKOUT
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel.Companion.GRAPH_REPEATS_PER_WORKOUT
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel.Companion.GRAPH_SETS_PER_WORKOUT
import com.pinhsiang.fitracker.workout.analysis.WorkoutAnalysisViewModel.Companion.GRAPH_VOLUME_PER_WORKOUT

class WorkoutAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutAnalysisBinding
    private lateinit var chart: LineChart
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis

    /**
     * Lazily initialize our [WorkoutAnalysisViewModel].
     */
    private val viewModel by viewModels<WorkoutAnalysisViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutAnalysisBinding.inflate(inflater, container, false)

        setupLineChart()
        setupXAxis()
        setupYAxis()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup spinners
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
                    0 -> {
                        viewModel.setExerciseFilter(getString(R.string.bench_press))
                    }
                    1 -> {
                        viewModel.setExerciseFilter(getString(R.string.deadlift))
                    }
                    2 -> {
                        viewModel.setExerciseFilter(getString(R.string.squat))
                    }
                }
            }
        }

        val graphList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.graph_workout,
            R.layout.spinner_item
        )
        binding.spinnerGraph.adapter = graphList
        binding.spinnerGraph.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.setGraphFilter(GRAPH_MAX_WEIGHT_PER_WORKOUT)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.setGraphFilter(GRAPH_MAX_WEIGHT_PER_WORKOUT)
                    }
                    1 -> {
                        viewModel.setGraphFilter(GRAPH_VOLUME_PER_WORKOUT)
                    }
                    2 -> {
                        viewModel.setGraphFilter(GRAPH_SETS_PER_WORKOUT)
                    }
                    3 -> {
                        viewModel.setGraphFilter(GRAPH_REPEATS_PER_WORKOUT)
                    }
                }
            }
        }

        viewModel.isDataReadyForPlotting.observe(this, Observer {
            if (it) {
                plotData(viewModel.xAxisDateToPlot, viewModel.valuesToPLot)
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

    private fun setupLineChart() {
        chart = binding.chartWorkout
        with(chart) {
            setBackgroundColor(Util.getColor(R.color.colorWhite))

            // Disable description text
            description.isEnabled = false
            legend.isEnabled = false

            // Disable touch gestures.
            setTouchEnabled(false)
            setDrawGridBackground(false)

            // Enable scaling and dragging.
            isDragEnabled = true
            setScaleEnabled(true)

            setDrawBorders(false)

            // Disable dual y-axis.
            axisRight.isEnabled = false
        }
    }

    private fun setupXAxis() {
        xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // vertical grid lines
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
    }

    private fun setupYAxis() {
        yAxis = chart.axisLeft
        yAxis.gridLineWidth = Y_AXIS_GRID_LINE_WIDTH
    }

    private fun plotData(xAxisDate: List<String>, values: List<Entry>) {

        val formatter = IndexAxisValueFormatter(xAxisDate)
        xAxis.valueFormatter = formatter

        var lineDataSet: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            lineDataSet = chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = values
            lineDataSet.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {

            lineDataSet = LineDataSet(values, "DataSet 1")

            with(lineDataSet) {
                setDrawIcons(false)
                color = getColor(R.color.colorBlack)
                lineWidth = 4f
                disableDashedLine()
                setDrawCircles(false)
                setDrawCircleHole(false)
                valueTextSize = 0f
                setDrawFilled(false)
            }

            xAxis.textSize = CHART_AXIS_TEXT_SIZE
            xAxis.labelRotationAngle = CHART_X_AXIS_LABEL_ROTATION
            yAxis.textSize = CHART_AXIS_TEXT_SIZE

            chart.data = LineData(listOf<ILineDataSet>(lineDataSet))
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