package com.example.quakesafe.mesh

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WifiP2pAdapter(private val context: Context) : MeshAdapter {
    override val name: String = "WiFiDirect"
    override var listener: MeshEventListener? = null
    private val manager: WifiP2pManager? = context.getSystemService(Context.WIFI_P2P_SERVICE) as? WifiP2pManager
    private val channel = manager?.initialize(context, context.mainLooper, null)
    private val activeNodes = MutableStateFlow<List<String>>(emptyList())

    @SuppressLint("MissingPermission")
    override fun startDiscovery() {
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {}
            override fun onFailure(reason: Int) {}
        })
    }

    override fun stopDiscovery() {
        manager?.stopPeerDiscovery(channel, null)
    }

    override fun sendMessage(targetId: String, payload: ByteArray) {
        // Implement socket-based communication after connection
    }

    override fun broadcastMessage(payload: ByteArray) {
        // Broadcast via GO (Group Owner) if connected
    }

    override fun getActiveNodes(): Flow<List<String>> = activeNodes

    override fun isSupported(): Boolean = manager != null
}
