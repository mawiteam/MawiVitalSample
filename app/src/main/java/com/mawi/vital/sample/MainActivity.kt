package com.mawi.vital.sample

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.mawi.vital.ble.MawiVital
import com.mawi.vital.ble.MawiVitalUtils
import com.mawi.vital.ble.callback.MawiVitalStateCallback
import com.mawi.vital.ble.model.BatteryState
import com.mawi.vital.ble.model.ConnectionState
import com.mawi.vital.sample.fragment.pairing.PairDeviceFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Error
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), MawiVitalStateCallback {

    companion object {
        private const val ENABLE_BT_RQ_CODE = 132
        private const val REQUES_REQUIRED_PERMISSIONS_RQ_CODE = 133
    }

    @Inject
    lateinit var mawiVital: MawiVital

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (MawiVitalUtils.isRequiredPermissionsGranted(this)) {

            if (MawiVitalUtils.isBluetoothEnabled(this)) {
                initialize()
                return
            }

            MawiVitalUtils.enableBluetooth(this, ENABLE_BT_RQ_CODE)

            return
        }

        MawiVitalUtils.requestRequiredPermissions(this, REQUES_REQUIRED_PERMISSIONS_RQ_CODE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mawiVital.removeStateCallback(this)
        mawiVital.unbind()
    }

    override fun onBattery(state: BatteryState, value: Int) {
        Log.d("MainActivity", "MawiVitalStateCallback::onBattery($state, $value)")
    }

    override fun onConnectionState(connectionState: ConnectionState) {
        Log.d("MainActivity", "MawiVitalStateCallback::onConnectionState($connectionState)")

    }

    override fun onDeviceInfo(serialNumber: String) {
        Log.d("MainActivity", "MawiVitalStateCallback::onDeviceInfo($serialNumber)")
    }

    override fun onFailure(exception: Error) {
        Log.e("MainActivity", "MawiVitalStateCallback::onFailure($exception)")
    }

    fun replace(fragment: Fragment) {
        supportFragmentManager.commit(true) {
            replace(R.id.container_view, fragment)
        }
    }

    private fun initialize() {
        mawiVital.addStateCallback(this)
        mawiVital.bind()

        supportFragmentManager.commit(allowStateLoss = true) {
            setReorderingAllowed(true)
            add<PairDeviceFragment>(R.id.container_view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != ENABLE_BT_RQ_CODE) return
        if (resultCode != Activity.RESULT_OK) return

        initialize()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUES_REQUIRED_PERMISSIONS_RQ_CODE) return

        if (!grantResults.all { it == PackageManager.PERMISSION_GRANTED }) return

        if (MawiVitalUtils.isBluetoothEnabled(this)) {
            initialize()
            return
        }

        MawiVitalUtils.enableBluetooth(this, ENABLE_BT_RQ_CODE)
    }

}