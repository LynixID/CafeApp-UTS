//package com.example.cafeapp
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.cafeapp.UserDatabase.User
//import com.example.cafeapp.UserDatabase.UserDao
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.IOException
//
//@RunWith(AndroidJUnit4::class) // Menjalankan pengujian menggunakan runner AndroidJUnit4
//class UserTestDatabase {
//    // Mendeklarasikan objek UserDao untuk mengakses operasi database terkait tabel User
//    private lateinit var userDao: UserDao
//    // Mendeklarasikan objek CafeDatabase untuk database Room
//    private lateinit var db: CafeDatabase
//
//    // Membuat objek User dengan data "staff"
//    private val staff = User(username = "staff", password = "staff", role = "staff")
//    // Membuat objek User dengan data "admin"
//    private val admin = User(username = "admin", password = "admin", role = "admin")
//
//    @Before // Anotasi ini menunjukkan bahwa fungsi berikut dijalankan sebelum pengujian
//    fun createDb() {
//        // Mendapatkan konteks aplikasi untuk pembuatan database
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        // Membuat database Room secara sementara di memori (in-memory database)
//        db = Room.inMemoryDatabaseBuilder(context, CafeDatabase::class.java)
//            .allowMainThreadQueries() // Mengizinkan query di thread utama (hanya untuk pengujian)
//            .build()
//        // Mendapatkan instance dari UserDao untuk melakukan operasi database
//        userDao = db.userDao()
//    }
//
//    @After // Anotasi ini menunjukkan bahwa fungsi berikut dijalankan setelah pengujian selesai
//    @Throws(IOException::class) // Menangani kemungkinan kesalahan IO saat menutup database
//    fun closeDb() {
//        // Menutup koneksi database setelah pengujian selesai
//        db.close()
//    }
//
//    @Test // Menandakan bahwa ini adalah metode pengujian
//    @Throws(Exception::class) // Menangani kemungkinan kesalahan yang muncul selama pengujian
//    fun insertAndRetrieve() {
//        // Menyisipkan data User "staff" dan "admin" ke dalam database
//        userDao.insert(staff, admin)
//        // Mengambil semua data User dari database
//        val users = userDao.getAllUsers()
//        // Memastikan jumlah user yang diambil dari database ada 2
//        assert(users.size == 2)
//    }
//}