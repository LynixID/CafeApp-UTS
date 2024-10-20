package com.example.cafeapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cafeapp.MakanDatabase.Makan
import com.example.cafeapp.MakanDatabase.MakanDAO
import com.example.cafeapp.MinumDatabase.Minum
import com.example.cafeapp.MinumDatabase.MinumDAO
import com.example.cafeapp.UserDatabase.User
import com.example.cafeapp.UserDatabase.UserDao

@Database(entities = [Makan::class, Minum::class, User::class], version = 6)
abstract class CafeDatabase: RoomDatabase() {
    abstract fun makanDao(): MakanDAO
    abstract fun minumDao(): MinumDAO
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun getInstance(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java, "cafe_database"
                )
                    .fallbackToDestructiveMigration().build()
                    .also { INSTANCE = it }
            }
        }
    }
}