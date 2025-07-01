package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CutDao {
    @Insert
    suspend fun insert(entry: CutEntry): Long

    @Query("SELECT * FROM cut_entries")
    suspend fun getAll(): List<CutEntry>
}
