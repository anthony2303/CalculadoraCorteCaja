package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WithdrawalDao {
    @Insert
    suspend fun insert(withdrawal: WithdrawalEntry)

    @Query("SELECT * FROM withdrawals")
    suspend fun getAll(): List<WithdrawalEntry>
}
