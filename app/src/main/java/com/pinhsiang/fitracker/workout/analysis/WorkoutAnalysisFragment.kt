package com.pinhsiang.fitracker.workout.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.databinding.FragmentWorkoutAnalysisBinding
import com.pinhsiang.fitracker.util.Util.getColor
import kotlin.math.pow


class WorkoutAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutAnalysisBinding
    private lateinit var chart: LineChart
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private lateinit var viewModelFactory: WorkoutAnalysisViewModelFactory
    private val viewModel: WorkoutAnalysisViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkoutAnalysisViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutAnalysisBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        viewModelFactory = WorkoutAnalysisViewModelFactory(application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup exercise spinner
        val exerciseList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.exercise,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinnerExercise.adapter = exerciseList
        binding.spinnerExercise.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.setWorkoutMotion(getString(R.string.deadlift))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.setWorkoutMotion(getString(R.string.bench_press))
                    }
                    1 -> {
                        viewModel.setWorkoutMotion(getString(R.string.deadlift))
                    }
                    2 -> {
                        viewModel.setWorkoutMotion(getString(R.string.squat))
                    }
                }
            }
        }

        setupLineChart()
        setupXAxis()
        setupYAxis()
        setData()


        return binding.root
    }

    private fun setupLineChart() {
        chart = binding.chartWorkout
        with(chart) {
            setBackgroundColor(getColor(R.color.colorWhite))

            // Disable description text
            description.isEnabled = false

            // Disable touch gestures.
            setTouchEnabled(false)
//            setOnChartValueSelectedListener(this)
            setDrawGridBackground(false)

            // Enable scaling and dragging.
            isDragEnabled = true
            setScaleEnabled(true)

            // Force pinch zoom along both axis.
            setPinchZoom(true)
        }
    }

    private fun setupXAxis() {
        xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f)
    }

    private fun setupYAxis() {
        yAxis = chart.axisLeft

        // Disable dual axis (only use LEFT axis).
        chart.axisRight.isEnabled = false
    }

    fun setData() {
        var values = mutableListOf<Entry>()

        var x = 0.0f
        val xLimit = 5.0f
        val xStep = (xLimit - x) / 100f
        for (i in 0..99) {
            val y = x.pow(2)
            values.add(Entry(x, y))
            x += xStep
        }

        val set1 = LineDataSet(values, "DataSet 1")
        with(set1) {
            setDrawIcons(false)
            enableDashedLine(10f, 5f, 0f)
            color = getColor(R.color.colorBlack)
            lineWidth = 1f
            circleRadius = 0f
            setDrawCircleHole(false)
            valueTextSize = 9f
            enableDashedHighlightLine(10f, 5f, 0f)
            setDrawFilled(true)
            fillColor = getColor(R.color.colorBlack)
        }

        chart.data = LineData(listOf<ILineDataSet>(set1))
    }
}