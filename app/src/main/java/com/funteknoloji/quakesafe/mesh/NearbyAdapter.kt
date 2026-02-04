package com.funteknoloji.quakesafe.mesh

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NearbyAdapter(private val context: Context) : MeshAdapter {
    override val name: String = "NearbyConnections"
    override var listener: MeshEventListener? = null
    private val connectionsClient = Nearby.getConnectionsClient(context)
    private val activeEndpoints = MutableStateFlow<List<String>>(emptyList())

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            payload.asBytes()?.let { listener?.onDataReceived(endpointId, it) }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                activeEndpoints.value += endpointId
                listener?.onNodeConnected(endpointId)
            }
        }

        override fun onDisconnected(endpointId: String) {
            activeEndpoints.value -= endpointId
            listener?.onNodeDisconnected(endpointId)
        }
    }

    override fun startDiscovery() {
        val options = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        connectionsClient.startDiscovery("com.funteknoloji.quakesafe", object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                connectionsClient.requestConnection("User", endpointId, connectionLifecycleCallback)
            }
            override fun onEndpointLost(endpointId: String) {}
        }, options)

        val advOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        connectionsClient.startAdvertising("User", "com.funteknoloji.quakesafe", connectionLifecycleCallback, advOptions)
    }

    override fun stopDiscovery() {
        connectionsClient.stopDiscovery()
        connectionsClient.stopAdvertising()
        connectionsClient.stopAllEndpoints()
    }

    override fun sendMessage(targetId: String, payload: ByteArray) {
        connectionsClient.sendPayload(targetId, Payload.fromBytes(payload))
    }

    override fun broadcastMessage(payload: ByteArray) {
        activeEndpoints.value.forEach { sendMessage(it, payload) }
    }

    override fun getActiveNodes(): Flow<List<String>> = activeEndpoints

    override fun isSupported(): Boolean = true // Most modern Android devices support it
}
