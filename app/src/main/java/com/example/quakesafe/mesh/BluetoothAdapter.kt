package com.example.quakesafe.mesh

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BluetoothAdapter(private val context: Context) : MeshAdapter {
    override val name: String = "Bluetooth"
    private val activeNodes = MutableStateFlow<List<String>>(emptyList())

    override fun startDiscovery() {
        // BLE Scan & Advertise
    }

    override fun stopDiscovery() {}

    override fun sendMessage(targetId: String, payload: ByteArray) {}

    override fun broadcastMessage(payload: ByteArray) {}

    override fun getActiveNodes(): Flow<List<String>> = activeNodes

    override fun isSupported(): Boolean = true
}
