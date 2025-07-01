package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeliveryDao {
  @Insert
  suspend fun insert(entry: DeliveryEntry)

  @Query("SELECT * FROM deliveries WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
  suspend fun getEntriesInRange(from: Long, to: Long): List<DeliveryEntry>
}
