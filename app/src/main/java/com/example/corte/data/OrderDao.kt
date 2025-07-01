package com.example.corte.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntry)

    @Query("SELECT * FROM orders")
    suspend fun getAll(): List<OrderEntry>
}
