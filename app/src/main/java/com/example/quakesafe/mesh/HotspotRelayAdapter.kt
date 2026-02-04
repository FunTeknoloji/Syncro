package com.example.quakesafe.mesh

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HotspotRelayAdapter(private val context: Context) : MeshAdapter {
    override val name: String = "HotspotRelay"
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var hotspotReservation: WifiManager.LocalOnlyHotspotReservation? = null
    private val activeNodes = MutableStateFlow<List<String>>(emptyList())

    @SuppressLint("MissingPermission")
    override fun startDiscovery() {
        // Hotspot is usually used when this node acts as a central hub (repeater)
        wifiManager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {
            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                hotspotReservation = reservation
                // SSID and Password are in reservation.wifiConfiguration
            }
            override fun onFailed(reason: Int) {}
            override fun onStopped() { hotspotReservation = null }
        }, null)
    }

    override fun stopDiscovery() {
        hotspotReservation?.close()
    }

    override fun sendMessage(targetId: String, payload: ByteArray) {}

    override fun broadcastMessage(payload: ByteArray) {}

    override fun getActiveNodes(): Flow<List<String>> = activeNodes

    override fun isSupported(): Boolean = true
}
