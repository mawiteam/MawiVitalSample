package com.mawi.vital.sample.fragment.measure.bt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.TemperatureMeasureCallback
import com.mawi.vital.ble.model.TemperatureMeasure
import com.mawi.vital.ble.model.result.TemperatureMeasureResult
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentMeasureTemperatureBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TemperatureMeasureFragment : BaseFragment(R.layout.fragment_measure_temperature), TemperatureMeasureCallback {

    @Inject
    lateinit var mawiVital: MawiVital

    private val binding by lazy { FragmentMeasureTemperatureBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartMeasure.setOnClickListener {
            showProgressDialog()
            mawiVital.startMeasure(TemperatureMeasure(measuresCount = 1, callback = this))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressDialog()
        mawiVital.stopMeasure()
    }

    override fun onFailed() {
        Log.e("TemperatureMeasureFragment", "TemperatureMeasureCallback::onFailed()")
        hideProgressDialog()
    }

    override fun onCompleted(result: TemperatureMeasureResult) {
        Log.i("TemperatureMeasureFragment", "TemperatureMeasureCallback::onCompleted(${result.toString()})")
        hideProgressDialog()
        binding.tvValue.text = "${result.temperature} ºC"
    }

    override fun onProgress(percent: Int) {
        Log.w("TemperatureMeasureFragment", "TemperatureMeasureCallback::onProgress($percent)")
    }
}