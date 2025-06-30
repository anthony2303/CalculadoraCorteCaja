package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CutDao {
    @Insert
    suspend fun insertEntry(entry: CutEntry)

    @Query("SELECT * FROM cut_entries WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    suspend fun getEntriesInRange(start: Long, end: Long): List<CutEntry>
}
