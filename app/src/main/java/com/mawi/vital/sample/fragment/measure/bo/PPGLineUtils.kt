package com.mawi.vital.sample.fragment.measure.bo

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mawi.vital.sample.base.RingEntryBuffer

class PPGLineUtils(private val lineChart: LineChart) {

    private val buffer = RingEntryBuffer(1, 400, 0f)

    private val lineData = LineData()

    private var count = 0

    private var maxVisiblePoint = Float.MAX_VALUE
    private var minVisiblePoint = Float.MIN_VALUE

    init {
        lineChart.apply {
            legend.isEnabled = false
            isHighlightPerDragEnabled = false
            description.isEnabled = false
            isDragEnabled = true
            setTouchEnabled(false)
            setScaleEnabled(true)

            axisLeft.axisMaximum = maxVisiblePoint
            axisLeft.axisMinimum = minVisiblePoint
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            dragDecelerationFrictionCoef = 0.95f


            xAxis.setDrawLabels(false)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)

            xAxis.isEnabled = false

            setBackgroundColor(Color.TRANSPARENT)

            axisRight.isEnabled = false
        }

        lineData.addDataSet(getDataSet(buffer.buffer[0]))
        lineChart.data = lineData
    }


    fun addEntry(value: Double) {
        buffer.pushSamples(0, count++, value.toFloat())

        maxVisiblePoint = buffer.buffer[0].maxByOrNull { it.y }?.y ?: 0f
        minVisiblePoint = buffer.buffer[0].minByOrNull { it.y }?.y ?: 0f

        lineChart.axisLeft.axisMinimum = minVisiblePoint
        lineChart.axisLeft.axisMaximum = maxVisiblePoint

        lineChart.notifyDataSetChanged()
        lineChart.postInvalidate()
    }

    private fun getDataSet(entries: List<Entry>? = null): LineDataSet {
        val set = LineDataSet(entries, "")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ContextCompat.getColor(lineChart.context, android.R.color.holo_red_light)
        set.lineWidth = 1f
        set.setDrawCircles(false)
        set.setDrawValues(false)
        return set
    }
}