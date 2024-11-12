package com.example.cafeapp.UserDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cafeapp.MenuDatabase.KategoriConverter
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuDAO

@Database(entities = [Menu::class, User::class, Cart::class], version = 9, exportSchema = false)
@TypeConverters(KategoriConverter::class)
abstract class CafeDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDAO
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDAO

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun getInstance(context: Context): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafe_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
