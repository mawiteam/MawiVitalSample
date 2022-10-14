package com.mawi.vital.sample.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T: BaseIntent> : ViewModel() {

    private var intent: BaseIntent? = null

    abstract fun onTriggerEvent(intentType: T)

}