package com.pinhsiang.fitracker.nutrition.analysis

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
import com.pinhsiang.fitracker.databinding.FragmentNutritionAnalysisBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.util.Util.getColor
import com.pinhsiang.fitracker.util.Util.getDrawable

class NutritionAnalysisFragment : AnalysisBaseFragment() {

    private lateinit var binding: FragmentNutritionAnalysisBinding
    override lateinit var chart: LineChart
    override lateinit var xAxis: XAxis
    override lateinit var yAxis: YAxis

    /**
     * Lazily initialize our [NutritionAnalysisViewModel].
     */
    private val viewModel by viewModels<NutritionAnalysisViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionAnalysisBinding.inflate(inflater, container, false)

        chart = binding.chartNutrition
        xAxis = chart.xAxis
        yAxis = chart.axisLeft

        setupLineChart()
        setupXAxis()
        setupYAxis()
        setupNutrientSpinner()

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

    private fun setupNutrientSpinner() {

        val nutrientFilterList = ArrayAdapter.createFromResource(
            FitrackerApplication.appContext,
            R.array.nutrients,
            R.layout.spinner_item
        )

        binding.spinnerNutrients.adapter = nutrientFilterList
        binding.spinnerNutrients.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                viewModel.setNutrient(NutritionAnalysisViewModel.TOTAL_DAILY_ENERGY_EXTRACTED)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {
                    0 -> viewModel.setNutrient(NutritionAnalysisViewModel.TOTAL_DAILY_ENERGY_EXTRACTED)

                    1 -> viewModel.setNutrient(NutritionAnalysisViewModel.PROTEIN)

                    2 -> viewModel.setNutrient(NutritionAnalysisViewModel.CARBOHYDRATE)

                    3 -> viewModel.setNutrient(NutritionAnalysisViewModel.FAT)
                }
            }
        }
    }

    private fun setAllPeriodBtnOff() {

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

    private fun setPeriodBtnOn(period: Long) {

        when (period) {

            PERIOD_3M -> {
                binding.period3mNutrition.setTextColor(getColor(R.color.colorBackground))
                binding.period3mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            PERIOD_6M -> {
                binding.period6mNutrition.setTextColor(getColor(R.color.colorBackground))
                binding.period6mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            PERIOD_1Y -> {
                binding.period1yNutrition.setTextColor(getColor(R.color.colorBackground))
                binding.period1yNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            viewModel.currentTime -> {
                binding.periodAllNutrition.setTextColor(getColor(R.color.colorBackground))
                binding.periodAllNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
            }

            else -> {
                binding.period1mNutrition.setTextColor(getColor(R.color.colorBackground))
                binding.period1mNutrition.background = getDrawable(R.drawable.btn_text_border_inverse)
            }
        }
    }
}