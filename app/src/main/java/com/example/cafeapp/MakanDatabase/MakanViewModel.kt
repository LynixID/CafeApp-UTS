package com.example.cafeapp.MakanDatabase

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MakanViewModel(application: Application): AndroidViewModel(application) {

    private val makanDao: MakanDAO
    private val allMakans: LiveData<List<Makan>>

    init {
        val db = MakanDatabase.getInstance(application)
        makanDao = db.colorDao()
        allMakans = makanDao.getAll() // Mengambil semua data menu
    }

    fun getAllMakans(): LiveData<List<Makan>> {
        return allMakans
    }

    fun insertMakan(menu: Makan) {
        viewModelScope.launch(Dispatchers.IO) {
            makanDao.insert(menu)
        }
    }

    fun deleteMakanById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            makanDao.deleteById(id)
        }
    }

    fun getMakanByName(name: String): LiveData<Makan> {
        return makanDao.getMakanByName(name)
    }

    fun getMakanByHex(harga: Int): LiveData<Makan> {
        return makanDao.getMakanByHex(harga)
    }

    fun saveImageToInternalStorage(bitmap: Bitmap, imageName: String): String? {
        // Gunakan getApplication() untuk mengakses context
        val context = getApplication<Application>().applicationContext
        val directory = File(context.filesDir, "app_images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$imageName.jpg")
        return try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            file.name // Mengembalikan nama file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}