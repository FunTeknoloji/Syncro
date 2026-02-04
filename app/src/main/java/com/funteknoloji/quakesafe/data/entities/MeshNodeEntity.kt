package com.funteknoloji.quakesafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesh_nodes")
data class MeshNodeEntity(
    @PrimaryKey val nodeId: String,
    val name: String,
    val lastSeen: Long,
    val rssi: Int,
    val hopCount: Int,
    val batteryLevel: Int = -1,
    val isDirect: Boolean = true
)
