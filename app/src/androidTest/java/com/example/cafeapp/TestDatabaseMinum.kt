package com.example.cafeapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cafeapp.MinumDatabase.MinumDAO
import com.example.cafeapp.MinumDatabase.Minum
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TestDatabaseMinum {
    private lateinit var DaoMInum: MinumDAO
    private lateinit var db: CafeDatabase

    val esJeruk = Minum(_id = 1, name = "Es Jeyuk", harga = 5000, deskripsi = "Jeruk asam menyegarkan", namaFoto = "jeruk.png")
    val esTeh = Minum(_id = 2, name = "Es Teh", harga = 3000, deskripsi = "Teh Kota temani anda", namaFoto = "teh.png")


    @Before
        fun createDb() {
            val context: Context = ApplicationProvider.getApplicationContext()
            db = Room.inMemoryDatabaseBuilder(context, CafeDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        DaoMInum = db.minumDao()
        }
    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieve() {
        DaoMInum.insert(esJeruk, esTeh)
        val minums = DaoMInum.getAllData()
        assert(minums.size == 2)
    }
}