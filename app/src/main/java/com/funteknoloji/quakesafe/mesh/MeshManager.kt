package com.funteknoloji.quakesafe.mesh

import android.content.Context
import com.funteknoloji.quakesafe.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeshManager(private val context: Context) : MeshEventListener {
    private val adapters = mutableListOf<MeshAdapter>()

    private val _connectedNodes = MutableStateFlow<List<String>>(emptyList())
    val connectedNodes: StateFlow<List<String>> = _connectedNodes

    init {
        adapters.add(NearbyAdapter(context))
        adapters.add(WifiP2pAdapter(context))
        adapters.add(BluetoothAdapter(context))
        adapters.add(HotspotRelayAdapter(context))
        adapters.forEach { it.listener = this }
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

    override fun onNodeConnected(nodeId: String) {
        _connectedNodes.value += nodeId
    }

    override fun onNodeDisconnected(nodeId: String) {
        _connectedNodes.value -= nodeId
    }

    override fun onDataReceived(senderId: String, payload: ByteArray) {
        val decryptedPayload = if (sharedKeys.containsKey(senderId)) {
            try {
                SecurityManager.decrypt(payload, sharedKeys[senderId]!!)
            } catch (e: Exception) {
                payload
            }
        } else {
            payload
        }

        // Parse MeshPacket (Simplified: assume it's a JSON string for now if type is Text)
        val messageContent = String(decryptedPayload)
        // In a real app, you'd deserialize the MeshPacket object

        // Save to Database (should use a repository)
        val db = AppDatabase.getDatabase(context)
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            db.messageDao().insertMessage(
                com.funteknoloji.quakesafe.data.entities.MessageEntity(
                    senderId = senderId,
                    content = messageContent,
                    timestamp = System.currentTimeMillis(),
                    priority = 0,
                    isSent = false,
                    isDelivered = true
                )
            )
        }
    }

    // TODO: Implement multi-hop routing and packet relaying
}
