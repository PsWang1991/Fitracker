package com.pinhsiang.fitracker.nutrition.analysis

import android.os.Bundle
import android.util.Log
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
import com.pinhsiang.fitracker.databinding.FragmentNutritionAnalysisBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.util.Util

const val CHART_AXIS_TEXT_SIZE = 14f
const val CHART_X_AXIS_LABEL_ROTATION = 45f

class NutritionAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentNutritionAnalysisBinding
    private lateinit var chart: LineChart
    private lateinit var xAxis: XAxis
    private lateinit var yAxis: YAxis

    /**
     * Lazily initialize our [NutritionAnalysisViewModel].
     */
    private val viewModel by viewModels<NutritionAnalysisViewModel> {getVmFactory()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionAnalysisBinding.inflate(inflater, container, false)

        setupLineChart()
        setupXAxis()
        setupYAxis()

        // Pass dataTime from workout fragment to detail fragment
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup spinner
        val inbodyFilterList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.nutrients,
            R.layout.spinner_item
        )
        binding.spinnerNutrients.adapter = inbodyFilterList
        binding.spinnerNutrients.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.setNutrient(getString(R.string.total_daily_energy_extracted))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.setNutrient(getString(R.string.total_daily_energy_extracted))
                    }
                    1 -> {
                        viewModel.setNutrient(getString(R.string.protein))
                    }
                    2 -> {
                        viewModel.setNutrient(getString(R.string.carbohydrate))
                    }
                    3 -> {
                        viewModel.setNutrient(getString(R.string.fat))
                    }
                }
            }
        }

        viewModel.plotDataReady.observe(this, Observer {
            if (it) {
                setDataToChart(viewModel.xAxisDateToPlot, viewModel.valuesToPLot)
                chart.invalidate()
                viewModel.plotDataDone()
            }
        })

        viewModel.periodFilter.observe(this, Observer {
            it?.let {
                when (it) {
                    DAYS_PER_3M * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterNormal()
                        binding.period3mNutrition.setTextColor(Util.getColor(R.color.colorBackground))
                        binding.period3mNutrition.background =
                            Util.getDrawable(R.drawable.btn_text_border_inverse)
                    }
                    DAYS_PER_6M * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterNormal()
                        binding.period6mNutrition.setTextColor(Util.getColor(R.color.colorBackground))
                        binding.period6mNutrition.background =
                            Util.getDrawable(R.drawable.btn_text_border_inverse)
                    }
                    DAYS_PER_1Y * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterNormal()
                        binding.period1yNutrition.setTextColor(Util.getColor(R.color.colorBackground))
                        binding.period1yNutrition.background =
                            Util.getDrawable(R.drawable.btn_text_border_inverse)
                    }
                    viewModel.currentTime -> {
                        setAllPeriodFilterNormal()
                        binding.periodAllNutrition.setTextColor(Util.getColor(R.color.colorBackground))
                        binding.periodAllNutrition.background =
                            Util.getDrawable(R.drawable.btn_text_border_inverse)
                    }
                    else -> {
                        setAllPeriodFilterNormal()
                        binding.period1mNutrition.setTextColor(Util.getColor(R.color.colorBackground))
                        binding.period1mNutrition.background =
                            Util.getDrawable(R.drawable.btn_text_border_inverse)
                    }
                }
            }
        })

        return binding.root
    }

    private fun setupLineChart() {
        chart = binding.chartNutrition
        with(chart) {
            setBackgroundColor(com.pinhsiang.fitracker.util.Util.getColor(com.pinhsiang.fitracker.R.color.colorWhite))

            // Disable description text
            description.isEnabled = false
            legend.isEnabled = false

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

    private fun setDataToChart(xAxisDate: List<String>, values: List<Entry>) {

        val formatter = IndexAxisValueFormatter(xAxisDate)
        xAxis.valueFormatter = formatter

        var set1: LineDataSet


        Log.i(TAG, "chart.data = ${chart.data}")
        if (chart.data != null) {
            Log.i(TAG, "chart.data.dataSetCount = ${chart.data.dataSetCount}")
        }

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values

            Log.i(TAG, "chart.values (chart data != null, count > 1) = ${set1.values}")

            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {

            set1 = LineDataSet(values, "DataSet 1")

            Log.i(TAG, "chart.values (in else) = ${set1.values}")

            with(set1) {
                setDrawIcons(false)
                enableDashedLine(10f, 5f, 0f)
                color = getColor(R.color.colorBlack)
                lineWidth = 4f
                disableDashedLine()
//            circleRadius = 0f
//            setCircleColor(getColor(R.color.colorInvisible))
                setDrawCircles(false)
                setDrawCircleHole(false)
                valueTextSize = 0f
                enableDashedHighlightLine(10f, 5f, 0f)
                setDrawFilled(false)
            }

            xAxis.textSize = CHART_AXIS_TEXT_SIZE
            xAxis.labelRotationAngle = CHART_X_AXIS_LABEL_ROTATION
            yAxis.textSize = CHART_AXIS_TEXT_SIZE

            chart.data = LineData(listOf<ILineDataSet>(set1))
        }
    }

    private fun setAllPeriodFilterNormal() {
        binding.period1mNutrition.setTextColor(Util.getColor(R.color.colorText))
        binding.period1mNutrition.background = Util.getDrawable(R.drawable.btn_text_border)
        binding.period3mNutrition.setTextColor(Util.getColor(R.color.colorText))
        binding.period3mNutrition.background = Util.getDrawable(R.drawable.btn_text_border)
        binding.period6mNutrition.setTextColor(Util.getColor(R.color.colorText))
        binding.period6mNutrition.background = Util.getDrawable(R.drawable.btn_text_border)
        binding.period1yNutrition.setTextColor(Util.getColor(R.color.colorText))
        binding.period1yNutrition.background = Util.getDrawable(R.drawable.btn_text_border)
        binding.periodAllNutrition.setTextColor(Util.getColor(R.color.colorText))
        binding.periodAllNutrition.background = Util.getDrawable(R.drawable.btn_text_border)
    }
}