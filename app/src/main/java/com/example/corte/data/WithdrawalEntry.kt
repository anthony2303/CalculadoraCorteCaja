package com.example.corte.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withdrawals")
data class WithdrawalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val timestamp: Long
)
