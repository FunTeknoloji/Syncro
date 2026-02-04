package com.example.quakesafe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quakesafe.data.dao.MeshNodeDao
import com.example.quakesafe.data.dao.MessageDao
import com.example.quakesafe.data.entities.MeshNodeEntity
import com.example.quakesafe.data.entities.MessageEntity

@Database(entities = [MessageEntity::class, MeshNodeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun meshNodeDao(): MeshNodeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quake_safe_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
