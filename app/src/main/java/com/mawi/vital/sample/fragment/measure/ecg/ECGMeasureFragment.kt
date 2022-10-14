package com.mawi.vital.sample.fragment.measure.ecg

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.ECGMeasureCallback
import com.mawi.vital.ble.model.ECGMeasure
import com.mawi.vital.ble.model.result.ECGMeasureResult
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasureEcgBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ECGMeasureFragment : BaseFragment(R.layout.fragment_measure_ecg), ECGMeasureCallback {

    @Inject
    lateinit var mawiVital: MawiVital

    private val binding by lazy { FragmentMeasureEcgBinding.bind(requireView()) }

    private var ecgUtils: ECGLineUtils? = null

    private var isStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartMeasure.setOnClickListener {
            if (isStarted) {
                mawiVital.stopMeasure()
                binding.btnStartMeasure.text = "Start measure"
            } else {
                binding.btnStartMeasure.text = "Stop measure"
                mawiVital.startMeasure(ECGMeasure(60, true, this))
            }

            isStarted = !isStarted
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mawiVital.stopMeasure()
    }

    override fun onCompleted(result: ECGMeasureResult) {
        Log.i("ECGMeasureFragment", "ECGMeasureCallback::onCompleted($result)")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }

    override fun onECGSample(raw: Int, filtered: Double) {
        ecgUtils?.addEntry(filtered)
    }

    override fun onECGValidation(isValid: Boolean) {
        Log.i("ECGMeasureFragment", "ECGMeasureCallback::onECGValidation($isValid)")
    }

    override fun onHeartRate(heartRate: Int) {
        Log.i("ECGMeasureFragment", "ECGMeasureCallback::onHeartRate($heartRate)")
        binding.tvHrValue.text = "$heartRate BPM"
    }

    override fun onProgress(percent: Int, duration: Int) {
        Log.w("ECGMeasureFragment", "ECGMeasureCallback::onProgress($percent, $duration)")
    }

    override fun onRespiratoryRate(respiratoryRate: Int) {
        Log.i("ECGMeasureFragment", "ECGMeasureCallback::onRespiratoryRate($respiratoryRate)")
        binding.tvRrValue.text = "$respiratoryRate BRPM"
    }

    override fun onSamplingRate(samplingRate: Int) {
        Log.i("ECGMeasureFragment", "ECGMeasureCallback::onSamplingRate($samplingRate)")
        ecgUtils = ECGLineUtils(binding.ecgChartView, samplingRate * 2)
    }

    override fun onFailed() {
        Log.e("ECGMeasureFragment", "ECGMeasureCallback::onFailed()")
        binding.btnStartMeasure.text = "Start measure"
        isStarted = false
    }
}