package com.example.corte.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deliveries")
data class DeliveryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val efectivo: Double,
    val tpv: Double,
    val propinas: Double,
    val retiros: Double,
    val pedidos: Int
)
