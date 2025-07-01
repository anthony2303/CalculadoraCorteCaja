package com.example.corte.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CutEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun cutDao(): CutDao

  companion object {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room
          .databaseBuilder(context, AppDatabase::class.java, "corte-db")
          .build()
          .also { INSTANCE = it }
      }
  }
}
