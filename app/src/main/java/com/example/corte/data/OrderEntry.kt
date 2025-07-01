package com.example.corte.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val paymentType: String,
    val tip: Double = 0.0,
    val timestamp: Long
)
