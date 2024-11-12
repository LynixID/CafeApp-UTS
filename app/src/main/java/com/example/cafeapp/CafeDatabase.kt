package com.example.cafeapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cafeapp.MenuDatabase.KategoriConverter
import com.example.cafeapp.MenuDatabase.Menu
import com.example.cafeapp.MenuDatabase.MenuDAO
import com.example.cafeapp.Transaksi.Transaksi
import com.example.cafeapp.Transaksi.TransaksiDAO
import com.example.cafeapp.UserDatabase.User
import com.example.cafeapp.UserDatabase.UserDao

@Database(entities = [Menu::class, User::class, Transaksi::class], version = 9, exportSchema = false)
@TypeConverters(KategoriConverter::class)
abstract class CafeDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDAO
    abstract fun userDao(): UserDao
    abstract fun transaksiDao(): TransaksiDAO

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
