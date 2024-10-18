package com.example.cafeapp.TestDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Menu::class], version = 2)
abstract class MenuDatabase: RoomDatabase() {
    abstract fun colorDao(): MenuDAO

    companion object{
        @Volatile
        private var INSTANCE: MenuDatabase? = null

        fun getInstance(context: Context):MenuDatabase{
            return INSTANCE?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MenuDatabase::class.java, "menu_database"
                )
                    .fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}