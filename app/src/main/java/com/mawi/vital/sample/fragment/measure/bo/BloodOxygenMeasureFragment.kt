package com.mawi.vital.sample.fragment.measure.bo

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.BloodOxygenMeasureCallback
import com.mawi.vital.ble.model.BloodOxygenMeasure
import com.mawi.vital.ble.model.result.BloodOxygenMeasureResult
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasureBloodOxygenBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BloodOxygenMeasureFragment : BaseFragment(R.layout.fragment_measure_blood_oxygen), BloodOxygenMeasureCallback {

    @Inject
    lateinit var mawiVital: MawiVital

    private val binding by lazy { FragmentMeasureBloodOxygenBinding.bind(requireView()) }

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
                mawiVital.startMeasure(BloodOxygenMeasure(30, this))
                binding.btnStartMeasure.text = "Stop measure"
            }

            isStarted = !isStarted
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mawiVital.stopMeasure()
    }

    override fun onBloodOxygen(spo2: Int, heartRate: Int) {
        Log.i("BloodOxygenMeasureFragment", "BloodOxygenMeasureCallback::onBloodOxygen($spo2, $heartRate)")
        binding.tvHrValue.text = "$heartRate BPM"
        binding.tvSpo2Value.text = "$spo2%"
    }

    override fun onCalculationError() {
        Log.e("BloodOxygenMeasureFragment", "BloodOxygenMeasureCallback::onCalculationError()")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }

    override fun onCompleted(result: BloodOxygenMeasureResult) {
        Log.i("BloodOxygenMeasureFragment", "BloodOxygenMeasureCallback::onCompleted($result)")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
        binding.tvHrValue.text = "${result.heartRate} BPM"
        binding.tvSpo2Value.text = "${result.spo2}%"
    }

    override fun onPPGSample(sample: Int) {
        ppgUtils?.addEntry(sample.toDouble())
    }

    override fun onFailed() {
        Log.e("BloodOxygenMeasureFragment", "BloodOxygenMeasureCallback::onFailed()")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }
}