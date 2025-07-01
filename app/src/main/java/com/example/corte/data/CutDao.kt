package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CutDao {
  @Insert
  suspend fun insert(entry: CutEntry)

  @Query("SELECT * FROM cuts WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
  suspend fun getEntriesInRange(from: Long, to: Long): List<CutEntry>
}
