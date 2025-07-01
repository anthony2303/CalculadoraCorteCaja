package com.example.corte.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DeliveryEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun deliveryDao(): DeliveryDao

  companion object {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room
          .databaseBuilder(context, AppDatabase::class.java, "delivery-db")
          .build()
          .also { INSTANCE = it }
      }
  }
}
