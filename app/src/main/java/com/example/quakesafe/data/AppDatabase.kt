package com.example.quakesafe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quakesafe.data.dao.ContactDao
import com.example.quakesafe.data.dao.MeshNodeDao
import com.example.quakesafe.data.dao.MessageDao
import com.example.quakesafe.data.entities.ContactEntity
import com.example.quakesafe.data.entities.MeshNodeEntity
import com.example.quakesafe.data.entities.MessageEntity

@Database(entities = [MessageEntity::class, MeshNodeEntity::class, ContactEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun meshNodeDao(): MeshNodeDao
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quake_safe_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
