//package com.example.cafeapp.UserDatabase
//
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import android.content.Context
//
//@Database(entities = [User::class], version = 3)
//abstract class UserDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: UserDatabase? = null
//
//        fun getDatabase(context: Context): UserDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    UserDatabase::class.java,
//                    "user_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
