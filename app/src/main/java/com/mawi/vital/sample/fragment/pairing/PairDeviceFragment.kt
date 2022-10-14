package com.mawi.vital.sample.fragment.pairing

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mawi.vital.sample.MainActivity
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseFragment
import com.mawi.vital.sample.databinding.FragmentPairDeviceBinding
import com.mawi.vital.sample.fragment.measure.MeasureFragment
import com.mawi.vital.sample.fragment.pairing.PairDeviceViewModel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PairDeviceFragment : BaseFragment(R.layout.fragment_pair_device) {

    private val viewModel: PairDeviceViewModel by viewModels()

    private val binding by lazy { FragmentPairDeviceBinding.bind(requireView()) }

    private val contentAdapter = ScanResultsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.apply {
            adapter = contentAdapter
            layoutManager = LinearLayoutManager(view.context)
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner, ::handleState)
        viewModel.eventsLiveData.observe(viewLifecycleOwner, ::handleEvent)

        viewModel.startSearching()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopSearching()
    }

    private fun handleState(list: List<ScanResultItemModel>) {
        when {
            list.isEmpty() -> {
                binding.rvContent.isVisible = false
                binding.progressBar.isVisible = true
            }

            else -> {
                binding.progressBar.isVisible = false
                binding.rvContent.isVisible = true
                contentAdapter.submitList(list)
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            Event.ConnectPeripheralFailed -> {
                hideProgressDialog()
            }
            Event.ConnectPeripheralSucceeded -> {
                hideProgressDialog()
                (requireActivity() as MainActivity).replace(MeasureFragment())
            }
            Event.DropLoader -> showProgressDialog()
            Event.HideLoader -> hideProgressDialog()
        }
    }
}