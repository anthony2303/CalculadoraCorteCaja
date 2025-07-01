package com.example.corte.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuts")
data class CutEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val timestamp: Long
)
