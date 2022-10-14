package com.mawi.vital.sample.base

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<ItemModel>(view: View) : RecyclerView.ViewHolder(view) {

    protected var itemModel: ItemModel? = null

    @CallSuper
    open fun bind(itemModel: ItemModel) {
        this.itemModel = itemModel
    }
}