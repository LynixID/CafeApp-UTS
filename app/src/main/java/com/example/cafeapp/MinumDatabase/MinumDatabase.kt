package com.example.cafeapp.MinumDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cafeapp.Minum

@Database(entities = [Minum::class], version = 5)
abstract class MinumDatabase: RoomDatabase() {
    abstract fun minumDao(): MinumDAO

    companion object{
        @Volatile
        private var INSTANCE: MinumDatabase? = null

        fun getInstance(context: Context): MinumDatabase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MinumDatabase::class.java, "menu_database"
                )
                    .fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}