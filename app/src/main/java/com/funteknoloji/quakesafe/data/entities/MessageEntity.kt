package com.funteknoloji.quakesafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val priority: Int, // 0: Normal, 1: Urgent, 2: Emergency
    val isSent: Boolean = false,
    val isDelivered: Boolean = false
)
