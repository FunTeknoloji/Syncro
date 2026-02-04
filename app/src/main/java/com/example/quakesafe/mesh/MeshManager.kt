package com.example.quakesafe.mesh

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MeshManager(private val context: Context) {
    private val adapters = mutableListOf<MeshAdapter>()

    private val _connectedNodes = MutableStateFlow<List<String>>(emptyList())
    val connectedNodes: StateFlow<List<String>> = _connectedNodes

    init {
        // Initialize adapters
        // adapters.add(NearbyAdapter(context))
        // adapters.add(WifiP2pAdapter(context))
        // adapters.add(BluetoothAdapter(context))
    }

    fun startMesh() {
        adapters.forEach { if (it.isSupported()) it.startDiscovery() }
    }

    fun stopMesh() {
        adapters.forEach { it.stopDiscovery() }
    }

    private val sharedKeys = mutableMapOf<String, javax.crypto.SecretKey>()

    fun sendData(payload: ByteArray, targetId: String? = null) {
        val encryptedPayload = if (targetId != null && sharedKeys.containsKey(targetId)) {
            SecurityManager.encrypt(payload, sharedKeys[targetId]!!)
        } else {
            payload // Broadcasts are usually not E2EE in this simple version
        }

        if (targetId == null) {
            adapters.forEach { it.broadcastMessage(encryptedPayload) }
        } else {
            adapters.forEach { it.sendMessage(targetId, encryptedPayload) }
        }
    }

    fun onDataReceived(senderId: String, payload: ByteArray) {
        val decryptedPayload = if (sharedKeys.containsKey(senderId)) {
            try {
                SecurityManager.decrypt(payload, sharedKeys[senderId]!!)
            } catch (e: Exception) {
                payload
            }
        } else {
            payload
        }
        // Handle decrypted data (e.g., pass to VoicePTTService or MessageDao)
    }

    // TODO: Implement multi-hop routing and packet relaying
}
