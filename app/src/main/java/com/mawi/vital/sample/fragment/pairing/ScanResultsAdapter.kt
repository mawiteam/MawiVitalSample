package com.mawi.vital.sample.fragment.pairing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mawi.vital.sample.R
import com.mawi.vital.sample.base.BaseAdapter
import com.mawi.vital.sample.base.BaseItemModel
import com.mawi.vital.sample.base.BaseViewHolder
import com.mawi.vital.sample.databinding.ItemViewScanResultBinding


interface ScanResultItemModel : BaseItemModel<ScanResultItemModelParams>

data class ScanResultItemModelParams(
    val name: String,
    val address: String,
    val rssi: Int
)

class ScanResultsAdapter : BaseAdapter<ScanResultItemModel>() {

    override fun getViewHolder(parent: ViewGroup): BaseViewHolder<ScanResultItemModel> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_scan_result, parent, false)

        return ScanResultViewHolder(view)
    }

}

private class ScanResultViewHolder(view: View) :
    BaseViewHolder<ScanResultItemModel>(view) {

    private val binder = ItemViewScanResultBinding.bind(itemView)

    init {
        itemView.setOnClickListener { itemModel?.click() }
    }

    override fun bind(itemModel: ScanResultItemModel) {
        super.bind(itemModel)
        binder.tvPeripheralTypeName.text = itemModel.params.name
        binder.tvMacAddress.text = itemModel.params.address
        val signalLevel = (100f * (127f + itemModel.params.rssi) / (127f + 20f)).toInt()
        binder.ivSignalLvl.setImageLevel(signalLevel)
    }

}