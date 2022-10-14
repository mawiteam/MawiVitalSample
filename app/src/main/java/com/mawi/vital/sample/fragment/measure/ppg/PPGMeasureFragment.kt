package com.mawi.vital.sample.fragment.measure.ppg

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.PPGMeasureCallback
import com.mawi.vital.ble.model.PPGMeasure
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasurePpgBinding
import com.mawi.vital.sample.fragment.measure.bo.PPGLineUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PPGMeasureFragment : BaseFragment(R.layout.fragment_measure_ppg), PPGMeasureCallback {

    @Inject
    lateinit var mawiVital: MawiVital

    private val binding by lazy { FragmentMeasurePpgBinding.bind(requireView()) }

    private var ppgUtils: PPGLineUtils? = null

    private var isStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ppgUtils = PPGLineUtils(binding.ppgChartView)

        binding.btnStartMeasure.setOnClickListener {
            if (isStarted) {
                mawiVital.stopMeasure()
                binding.btnStartMeasure.text = "Start measure"
            } else {
                mawiVital.startMeasure(PPGMeasure(30, this))
                binding.btnStartMeasure.text = "Stop measure"
            }

            isStarted = !isStarted
        }
    }

    override fun onFailed() {
        Log.e("PPGMeasureFragment", "PPGMeasureCallback::onFailed()")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }

    override fun onCompleted(isValid: Boolean) {
        Log.e("PPGMeasureFragment", "PPGMeasureCallback::onCompleted($isValid)")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }

    override fun onPPGSample(sample: Int) {
        ppgUtils?.addEntry(sample.toDouble())
    }
}