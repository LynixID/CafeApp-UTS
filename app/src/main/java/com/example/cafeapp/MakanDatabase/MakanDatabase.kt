package com.example.cafeapp.MakanDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cafeapp.Makan

@Database(entities = [Makan::class], version = 4)
abstract class MakanDatabase: RoomDatabase() {
    abstract fun makanDao(): MakanDAO

    companion object{
        @Volatile
        private var INSTANCE: MakanDatabase? = null

        fun getInstance(context: Context):MakanDatabase{
            return INSTANCE?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MakanDatabase::class.java, "makan_database"
                )
                    .fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}