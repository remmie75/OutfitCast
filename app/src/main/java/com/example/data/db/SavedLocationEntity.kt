package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_locations")
data class SavedLocationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)
