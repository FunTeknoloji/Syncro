package com.funteknoloji.quakesafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val phoneNumber: String,
    val name: String,
    val isEmergencyContact: Boolean = false,
    val lastKnownLocation: String? = null
)
