package com.mawi.vital.sample.fragment.measure.bp

import android.os.Bundle
import android.util.Log
import android.view.View
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.BloodPressureMeasureCallback
import com.mawi.vital.ble.model.BloodPressureMeasure
import com.mawi.vital.ble.model.result.BloodPressureMeasureResult
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasureBloodPressureBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BloodPressureMeasureFragment : BaseFragment(R.layout.fragment_measure_blood_pressure), BloodPressureMeasureCallback {

    @Inject
    lateinit var mawiVital: MawiVital

    private val binding by lazy { FragmentMeasureBloodPressureBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartMeasure.setOnClickListener {
            showProgressDialog()
            mawiVital.startMeasure(BloodPressureMeasure(this))
        }
    }

    override fun onCompleted(result: BloodPressureMeasureResult) {
        Log.i("BloodPressureMeasureFragment", "BloodPressureMeasureCallback::onCompleted($result)")
        hideProgressDialog()
        binding.tvSystolicValue.text = "${result.systolic} mmHg"
        binding.tvDiastolicValue.text = "${result.diastolic} mmHg"
        binding.tvHrValue.text = "${result.heartRate} BPM"
    }

    override fun onLeakError() {
        Log.e("BloodPressureMeasureFragment", "BloodPressureMeasureCallback::onLeakError()")
        hideProgressDialog()
        // handle leak error
    }

    override fun onResultError() {
        Log.e("BloodPressureMeasureFragment", "BloodPressureMeasureCallback::onResultError()")
        hideProgressDialog()
        // handle result error
    }

    override fun onFrequentMeasures(remainTime: Int) {
        Log.e("BloodPressureMeasureFragment", "BloodPressureMeasureCallback::onFrequentMeasures($remainTime)")
        hideProgressDialog()
        // handle this error
    }

    override fun onFailed() {
        Log.e("BloodPressureMeasureFragment", "BloodPressureMeasureCallback::onFailed()")
        hideProgressDialog()
        // handle failure
    }
}