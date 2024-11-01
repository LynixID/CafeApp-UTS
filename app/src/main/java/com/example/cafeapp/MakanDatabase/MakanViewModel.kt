package com.example.cafeapp.MakanDatabase

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.UserDatabase.CafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MakanViewModel(application: Application): AndroidViewModel(application) {
    private val makanDao: MakanDAO
    private val allMakans: LiveData<List<Makan>>

    private val _filteredMakans = MutableLiveData<List<Makan>>() // Untuk hasil filter
    val filteredMakans: LiveData<List<Makan>> get() = _filteredMakans

    // Definisi urutan sorting
    enum class SortOrder { A_TO_Z, Z_TO_A }

    init {
        val db = CafeDatabase.getInstance(application)
        makanDao = db.makanDao()
        allMakans = makanDao.getAll() // Mengambil semua data menu

        // Observe allMakans untuk selalu update filteredMakans
        allMakans.observeForever { makans ->
            _filteredMakans.value = makans
        }
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
    // Metode untuk mendapatkan makanan berdasarkan ID
    fun getMakanById(id: Int): LiveData<Makan> {
        return makanDao.getMakanById(id) // Metode ini perlu ditambahkan di DAO
    }

    // Fungsi untuk update data Makan
    fun updateMakan(id: Int, name: String, harga: Double, deskripsi: String, namaFoto: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            makanDao.updateMakan(id, name, harga, deskripsi, namaFoto)
        }
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
    fun searchItems(query: String) {
        val filteredList = allMakans.value?.filter {
            it.name.contains(query, ignoreCase = true)
        } ?: emptyList() // Mengembalikan list kosong jika null
        _filteredMakans.value = filteredList
    }

    // Sort items A-Z or Z-A
    fun sortItems(order: SortOrder) {
        val sortedList = when (order) {
            SortOrder.A_TO_Z -> _filteredMakans.value?.sortedBy { it.name }
            SortOrder.Z_TO_A -> _filteredMakans.value?.sortedByDescending { it.name }
        } ?: emptyList()
        _filteredMakans.value = sortedList
    }

    fun filterByCategory(category: String) {
        val filteredList = allMakans.value?.filter { it.category == category } ?: emptyList()
        _filteredMakans.value = filteredList
    }
}