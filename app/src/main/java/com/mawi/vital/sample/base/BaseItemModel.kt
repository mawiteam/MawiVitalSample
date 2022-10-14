package com.mawi.vital.sample.base

interface BaseItemModel<Params> {

    val stableId: Long

    val params: Params

    fun click()
}