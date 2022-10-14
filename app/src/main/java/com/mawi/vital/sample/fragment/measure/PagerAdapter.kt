package com.mawi.vital.sample.fragment.measure

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mawi.vital.sample.fragment.measure.bo.BloodOxygenMeasureFragment
import com.mawi.vital.sample.fragment.measure.bp.BloodPressureMeasureFragment
import com.mawi.vital.sample.fragment.measure.bt.TemperatureMeasureFragment
import com.mawi.vital.sample.fragment.measure.ecg.ECGMeasureFragment
import com.mawi.vital.sample.fragment.measure.ppg.PPGMeasureFragment
import java.lang.IllegalStateException

class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TemperatureMeasureFragment()
        1 -> BloodPressureMeasureFragment()
        2 -> BloodOxygenMeasureFragment()
        3 -> ECGMeasureFragment()
        4 -> PPGMeasureFragment()
        else -> throw IllegalStateException("Index out of bounds.")
    }
}