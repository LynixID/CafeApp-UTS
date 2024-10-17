package com.example.cafeapp.TestDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Menu::class], version = 2)
abstract class ColorDatabase: RoomDatabase() {
    abstract fun colorDao(): MenuDAO

    companion object{
        @Volatile
        private var INSTANCE: ColorDatabase? = null

        fun getInstance(context: Context):ColorDatabase{
            return INSTANCE?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ColorDatabase::class.java, "color_database"
                )
                    .fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}