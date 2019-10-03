package com.pinhsiang.fitracker.nutrition.analysis

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
import com.pinhsiang.fitracker.databinding.FragmentNutritionAnalysisBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.util.Util
import com.pinhsiang.fitracker.util.Util.getColor
import com.pinhsiang.fitracker.util.Util.getDrawable

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup spinner
        val nutrientFilterList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.nutrients,
            R.layout.spinner_item
        )
        binding.spinnerNutrients.adapter = nutrientFilterList
        binding.spinnerNutrients.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.setNutrient(TOTAL_DAILY_ENERGY_EXTRACTED)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.setNutrient(TOTAL_DAILY_ENERGY_EXTRACTED)
                    }
                    1 -> {
                        viewModel.setNutrient(PROTEIN)
                    }
                    2 -> {
                        viewModel.setNutrient(CARBOHYDRATE)
                    }
                    3 -> {
                        viewModel.setNutrient(FAT)
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
                when (it) {
                    DAYS_PER_3M * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterUnselected()
                        selectPeriod3M()
                    }
                    DAYS_PER_6M * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterUnselected()
                        selectPeriod6M()
                    }
                    DAYS_PER_1Y * MILLISECOND_PER_DAY -> {
                        setAllPeriodFilterUnselected()
                        selectPeriod1Y()
                    }
                    viewModel.currentTime -> {
                        setAllPeriodFilterUnselected()
                        selectPeriodAll()
                    }
                    else -> {
                        setAllPeriodFilterUnselected()
                        selectPeriod1M()
                    }
                }
            }
        })

        return binding.root
    }

    private fun selectPeriod1M() {
        binding.period1mNutrition.setTextColor(getColor(R.color.colorBackground))
        binding.period1mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
    }

    private fun selectPeriod3M() {
        binding.period3mNutrition.setTextColor(getColor(R.color.colorBackground))
        binding.period3mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
    }

    private fun selectPeriod6M() {
        binding.period6mNutrition.setTextColor(getColor(R.color.colorBackground))
        binding.period6mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
    }

    private fun selectPeriod1Y() {
        binding.period1yNutrition.setTextColor(getColor(R.color.colorBackground))
        binding.period1yNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
    }

    private fun selectPeriodAll() {
        binding.periodAllNutrition.setTextColor(getColor(R.color.colorBackground))
        binding.periodAllNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
    }

    private fun setupLineChart() {
        chart = binding.chartNutrition
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

    private fun setAllPeriodFilterUnselected() {
        binding.period1mNutrition.setTextColor(getColor(R.color.colorText))
        binding.period1mNutrition.background = getDrawable(R.drawable.btn_text_border)
        binding.period3mNutrition.setTextColor(getColor(R.color.colorText))
        binding.period3mNutrition.background = getDrawable(R.drawable.btn_text_border)
        binding.period6mNutrition.setTextColor(getColor(R.color.colorText))
        binding.period6mNutrition.background = getDrawable(R.drawable.btn_text_border)
        binding.period1yNutrition.setTextColor(getColor(R.color.colorText))
        binding.period1yNutrition.background = getDrawable(R.drawable.btn_text_border)
        binding.periodAllNutrition.setTextColor(getColor(R.color.colorText))
        binding.periodAllNutrition.background = getDrawable(R.drawable.btn_text_border)
    }
}