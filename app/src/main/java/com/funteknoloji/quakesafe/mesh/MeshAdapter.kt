package com.funteknoloji.quakesafe.mesh

import kotlinx.coroutines.flow.Flow

interface MeshAdapter {
    val name: String
    var listener: MeshEventListener?
    fun startDiscovery()
    fun stopDiscovery()
    fun sendMessage(targetId: String, payload: ByteArray)
    fun broadcastMessage(payload: ByteArray)
    fun getActiveNodes(): Flow<List<String>>
    fun isSupported(): Boolean
}

interface MeshEventListener {
    fun onDataReceived(senderId: String, payload: ByteArray)
    fun onNodeConnected(nodeId: String)
    fun onNodeDisconnected(nodeId: String)
}

data class MeshPacket(
    val type: Int, // 0: Text, 1: Voice, 2: Control, 3: Routing
    val senderId: String,
    val targetId: String?, // Null for broadcast
    val hopCount: Int,
    val ttl: Int,
    val payload: ByteArray,
    val signature: ByteArray? = null
)
