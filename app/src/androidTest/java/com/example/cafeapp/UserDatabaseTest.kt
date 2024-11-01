//package com.example.cafeapp
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.cafeapp.UserDatabase.User
//import com.example.cafeapp.UserDatabase.UserDao
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.IOException
//import org.junit.Assert.*
//
//@RunWith(AndroidJUnit4::class)
//class UserDatabaseTest {
//
//    private lateinit var userDao: UserDao
//    private lateinit var db: CafeDatabase
//
//    private val staff = User(username = "staff", password = "staff", role = "staff")
//    private val admin = User(username = "admin", password = "admin", role = "admin")
//
//    @Before
//    fun createDb() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        db = Room.inMemoryDatabaseBuilder(context, CafeDatabase::class.java)
//            .build() // Hapus allowMainThreadQueries
//        userDao = db.userDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() = db.close()
//
//    @Test
//    @Throws(Exception::class)
//    fun insertAndRetrieve() = runBlocking {
//        userDao.insert(staff) // Tambahkan runBlocking di sini
//        userDao.insert(admin)
//        val users = userDao.getAllUsers()
//        assert(users.size == 2)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun deleteUsers() = runBlocking {
//        // Mengambil semua pengguna dari database sebelum pengujian
//        val usersBefore = userDao.getAllUsers()
//
//        // Menghapus semua pengguna yang ada untuk memastikan database kosong
//        for (user in usersBefore) {
//            userDao.delete(user)
//        }
//
//        // Mengambil semua pengguna setelah penghapusan
//        val users = userDao.getAllUsers()
//
//        // Memastikan daftar pengguna kosong
//        assertTrue(users.isEmpty())
//    }
//}
