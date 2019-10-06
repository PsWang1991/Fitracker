package com.pinhsiang.fitracker.base

import android.graphics.Typeface
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.pinhsiang.fitracker.CHART_AXIS_TEXT_SIZE
import com.pinhsiang.fitracker.CHART_X_AXIS_LABEL_ROTATION
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.Y_AXIS_GRID_LINE_WIDTH
import com.pinhsiang.fitracker.util.Util.getColor

abstract class AnalysisBaseFragment : Fragment() {

    protected abstract val chart: LineChart
    protected abstract val xAxis: XAxis
    protected abstract val yAxis: YAxis

    protected fun setupLineChart() {

        with(chart) {

            setBackgroundColor(getColor(R.color.colorWhite))
            setDrawGridBackground(false)

            description.isEnabled = false
            legend.isEnabled = false

            setTouchEnabled(false)

            isDragEnabled = true
            setScaleEnabled(true)

            setDrawBorders(true)
            setBorderColor(getColor(R.color.colorText))
            setBorderWidth(2f)

            // Only use single (left) y-axis.
            axisRight.isEnabled = false

            // Avoid x-axis text being cut by edge of line chart.
            setExtraOffsets(8f, 0f, 8f, 8f)
        }
    }

    protected fun setupXAxis() {

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        xAxis.typeface = Typeface.DEFAULT_BOLD
        xAxis.textSize = CHART_AXIS_TEXT_SIZE
        xAxis.labelRotationAngle = CHART_X_AXIS_LABEL_ROTATION
        xAxis.textColor = getColor(R.color.colorText)
    }

    protected fun setupYAxis() {

        yAxis.setDrawGridLines(true)
        yAxis.gridLineWidth = Y_AXIS_GRID_LINE_WIDTH
        yAxis.enableGridDashedLine(8f, 8f, 0f)
        yAxis.gridColor = getColor(R.color.colorItem)

        yAxis.typeface = Typeface.DEFAULT_BOLD
        yAxis.textSize = CHART_AXIS_TEXT_SIZE
        yAxis.textColor = getColor(R.color.colorText)

        yAxis.axisLineColor = getColor(R.color.colorInvisible)
    }

    protected fun plotData(x: List<String>, y: List<Entry>) {

        val formatter = IndexAxisValueFormatter(x)
        xAxis.valueFormatter = formatter

        val lineDataSet: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {

            lineDataSet = chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = y
            lineDataSet.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

        } else {

            lineDataSet = LineDataSet(y, "DataSet 1")

            with(lineDataSet) {
                setDrawIcons(false)
                color = getColor(R.color.colorText)
                lineWidth = 4f
                disableDashedLine()
                setDrawCircles(false)
                setDrawCircleHole(false)
                valueTextSize = 0f
                setDrawFilled(false)
            }

            chart.data = LineData(listOf<ILineDataSet>(lineDataSet))
        }
    }

}