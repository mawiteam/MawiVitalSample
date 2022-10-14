package com.mawi.vital.sample.fragment.measure

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.sample.MainActivity
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasureBinding
import com.mawi.vital.sample.fragment.pairing.PairDeviceFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MeasureFragment : BaseFragment(R.layout.fragment_measure), TabLayoutMediator.TabConfigurationStrategy {

    @Inject
    lateinit var mawiVital: MawiVital

    private var mediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMeasureBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener {
            mawiVital.disconnect()
            (requireActivity() as MainActivity).replace(PairDeviceFragment())
        }

        binding.viewPager.adapter = PagerAdapter(requireActivity())

        mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager, this)

        mediator?.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = when (position) {
            0 -> "Temperature"
            1 -> "Blood pressure"
            2 -> "Blood oxygen"
            3 -> "ECG"
            4 -> "PPG"
            else -> return
        }
    }
}