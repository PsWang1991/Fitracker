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
import com.pinhsiang.fitracker.util.Util

abstract class AnalysisBaseFragment : Fragment() {

    abstract val chart: LineChart
    abstract val xAxis: XAxis
    abstract val yAxis: YAxis

    protected fun setupLineChart() {

        with(chart) {
            setBackgroundColor(com.pinhsiang.fitracker.util.Util.getColor(com.pinhsiang.fitracker.R.color.colorWhite))

            // Disable description text
            description.isEnabled = false
            legend.isEnabled = false

            // Disable touch gestures.
            setTouchEnabled(false)
            setDrawGridBackground(false)

            // Enable scaling and dragging.
            isDragEnabled = true
            setScaleEnabled(true)

            setDrawBorders(true)
            setBorderColor(com.pinhsiang.fitracker.util.Util.getColor(com.pinhsiang.fitracker.R.color.colorText))
            setBorderWidth(2f)

            // Disable dual y-axis.
            axisRight.isEnabled = false

            setExtraOffsets(8f, 0f, 8f, 8f)
        }
    }

    private fun setupXAxis() {
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // vertical grid lines
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.typeface = Typeface.DEFAULT_BOLD
        xAxis.textSize = CHART_AXIS_TEXT_SIZE
        xAxis.labelRotationAngle = CHART_X_AXIS_LABEL_ROTATION
        xAxis.textColor = Util.getColor(R.color.colorText)
    }

    private fun setupYAxis() {
        yAxis.setDrawGridLines(true)
        yAxis.gridLineWidth = Y_AXIS_GRID_LINE_WIDTH
        yAxis.enableGridDashedLine(8f, 8f, 0f)
        yAxis.gridColor = Util.getColor(R.color.colorItem)
        yAxis.typeface = Typeface.DEFAULT_BOLD
        yAxis.axisLineColor = Util.getColor(R.color.colorInvisible)
        yAxis.textSize = CHART_AXIS_TEXT_SIZE
        yAxis.textColor = Util.getColor(R.color.colorText)
    }

    private fun plotData(x: List<String>, y: List<Entry>) {

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