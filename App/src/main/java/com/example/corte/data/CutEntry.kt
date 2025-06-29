package com.example.corte.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cut_entries")
data class CutEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val efectivo: Double,
    val tpv: Double,
    val propinas: Double,
    val retiros: Double,
    val pedidos: Int
)