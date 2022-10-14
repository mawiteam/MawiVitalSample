package com.mawi.vital.sample.base

import com.github.mikephil.charting.data.Entry

class RingEntryBuffer(
    arrayCount: Int,
    private val depth: Int,
    private val initialValue: Float
) {

    val buffer: MutableList<List<Entry>> = mutableListOf()

    init {
        repeat((0 until arrayCount).count()) {
            val samples = ArrayList<Entry>(depth)
            populate(samples, initialValue, depth)
            buffer.add(samples)
        }
    }

    private fun populate(list: MutableList<Entry>, initialValue: Float, count: Int) {
        (0 until count).forEach {
            list.add(Entry(it.toFloat(), initialValue))
        }
    }

    private fun updateEntry(list: List<Entry>, index: Float, value: Float) {
        val offset = index.toInt() % depth
        list[offset].x = offset.toFloat()
        list[offset].y = value
    }

    fun pushSamples(index: Int = 0, sampleIndex: Int, vararg values: Float) {
        updateEntry(buffer[index], sampleIndex.toFloat(), values[0])
    }

    fun getValuesAt(index: Int): FloatArray {
        val values = FloatArray(buffer.size)
        (0 until buffer.size).forEach {
            values[it] = buffer[it][index % depth].y
        }

        return values
    }
}