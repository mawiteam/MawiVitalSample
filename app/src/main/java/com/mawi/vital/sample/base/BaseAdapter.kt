package com.mawi.vital.sample.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<ItemModel : BaseItemModel<*>> :
    ListAdapter<ItemModel, BaseViewHolder<ItemModel>>(DiffUtils<ItemModel>()) {

    override fun getItemId(position: Int): Long {
        return getItem(position).stableId
    }

    abstract fun getViewHolder(parent: ViewGroup): BaseViewHolder<ItemModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemModel> {
        return getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemModel>, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DiffUtils<ItemModel : BaseItemModel<*>> : DiffUtil.ItemCallback<ItemModel>() {

    override fun areItemsTheSame(
        oldItem: ItemModel,
        newItem: ItemModel
    ): Boolean {
        return oldItem.stableId == newItem.stableId
    }

    override fun areContentsTheSame(
        oldItem: ItemModel,
        newItem: ItemModel
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}