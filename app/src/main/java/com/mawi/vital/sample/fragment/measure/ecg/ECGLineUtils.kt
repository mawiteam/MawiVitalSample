package com.mawi.vital.sample.fragment.measure.ecg

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mawi.vital.sample.base.RingEntryBuffer

class ECGLineUtils(private val lineChart: LineChart, private val xRange: Int) {

    private val buffer = RingEntryBuffer(1, xRange, 0f)

    private val lineData = LineData()

    private var count = 0

    init {
        lineChart.apply {
            legend.isEnabled = false
            isHighlightPerDragEnabled = false
            description.isEnabled = false
            isDragEnabled = true
            setTouchEnabled(false)
            setScaleEnabled(true)

            axisLeft.axisMaximum = 1.5f
            axisLeft.axisMinimum = -1.5f
            axisLeft.isEnabled = true
            axisRight.isEnabled = false
            dragDecelerationFrictionCoef = 0.95f


            xAxis.setDrawLabels(false)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)

            xAxis.isEnabled = false

            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$value mV"
                }
            }

            setBackgroundColor(Color.TRANSPARENT)

            axisRight.isEnabled = false
        }

        lineData.addDataSet(getDataSet(buffer.buffer[0]))
        lineChart.data = lineData
    }

    fun addEntry(value: Double) {
        buffer.pushSamples(0, count++, value.toFloat())
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