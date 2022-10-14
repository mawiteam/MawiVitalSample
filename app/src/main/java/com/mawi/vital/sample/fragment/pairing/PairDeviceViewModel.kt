package com.mawi.vital.sample.fragment.pairing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.callback.MawiVitalStateCallback
import com.mawi.vital.ble.callback.ScannerCallback
import com.mawi.vital.ble.model.BatteryState
import com.mawi.vital.ble.model.ConnectionState
import com.mawi.vital.ble.model.MawiVitalScanResult
import com.mawi.vital.sample.fragment.pairing.PairDeviceViewModel.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class PairDeviceViewModel @Inject constructor(
    private val mawiVital: MawiVital
) : ViewModel(), ScannerCallback, MawiVitalStateCallback {

    private inner class Model(
        override val params: ScanResultItemModelParams
    ) : ScanResultItemModel {

        override val stableId: Long
            get() = params.address.hashCode().toLong()

        override fun click() = connectPeripheral(params.address)
    }

    sealed class Event {
        object ConnectPeripheralSucceeded : Event()
        object ConnectPeripheralFailed : Event()
        object DropLoader : Event()
        object HideLoader : Event()
    }

    private val _stateLiveData = MutableLiveData<List<ScanResultItemModel>>()

    private val _eventsLiveData = MutableLiveData<Event>()

    val stateLiveData: LiveData<List<ScanResultItemModel>>
        get() = _stateLiveData

    val eventsLiveData: LiveData<Event>
        get() = _eventsLiveData

    fun startSearching() {
        mawiVital.addScannerCallback(this)
    }

    fun stopSearching() {
        mawiVital.removeScannerCallback(this)
    }

    override fun onCleared() {
        super.onCleared()
        mawiVital.removeStateCallback(this)
        mawiVital.removeScannerCallback(this)
    }

    override fun onScanResult(peripherals: List<MawiVitalScanResult>) {
        val models = peripherals.sortedByDescending { it.rssiLevel }.map { peripheral ->
            Model(
                ScanResultItemModelParams(peripheral.name,
                peripheral.macAddress, peripheral.rssiLevel)
            )
        }

        _stateLiveData.postValue(models)
    }

    override fun onBattery(state: BatteryState, value: Int) {

    }

    override fun onConnectionState(connectionState: ConnectionState) {
        when (connectionState) {
            ConnectionState.Connected -> {
                _eventsLiveData.postValue(HideLoader)
                _eventsLiveData.postValue(ConnectPeripheralSucceeded)
            }
        }
    }

    override fun onDeviceInfo(serialNumber: String) {
    }

    override fun onFailure(exception: Error) {
        _eventsLiveData.postValue(HideLoader)
        _eventsLiveData.postValue(ConnectPeripheralFailed)
    }

    private fun connectPeripheral(macAddress: String) {
        _eventsLiveData.postValue(DropLoader)
        mawiVital.addStateCallback(this)
        mawiVital.connect(macAddress)
    }
}