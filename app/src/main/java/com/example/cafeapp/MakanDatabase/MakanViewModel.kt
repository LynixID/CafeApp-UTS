package com.example.cafeapp.MakanDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.util.Log

class MakanViewModel(application: Application) : AndroidViewModel(application) {

    private val makanDao: MakanDAO
    val allMakans: LiveData<List<Makan>> // Semua makanan dari database

    private val _filteredMakans = MutableLiveData<List<Makan>>() // Untuk hasil filter
    val filteredMakans: LiveData<List<Makan>> get() = _filteredMakans

    // Definisi urutan sorting
    enum class SortOrder { A_TO_Z, Z_TO_A }

    init {
        val db = MakanDatabase.getInstance(application)
        makanDao = db.colorDao()
        allMakans = makanDao.getAll()

        // Observe allMakans untuk selalu update filteredMakans
        allMakans.observeForever { makans ->
            _filteredMakans.value = makans
        }
    }

    // Metode untuk mendapatkan makanan berdasarkan ID
    fun getMakanById(id: String): LiveData<Makan> { // Pastikan id adalah String
        return makanDao.getMakanById(id) // Metode ini perlu ditambahkan di DAO
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

    fun filterByCategory(category: String) {
        val filteredList = allMakans.value?.filter {
            it.kategori.equals(category, ignoreCase = true)
        } ?: emptyList()
        _filteredMakans.value = filteredList
    }

    // Pencarian item berdasarkan nama
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

    // Fungsi menyimpan gambar ke internal storage
    fun saveImageToInternalStorage(bitmap: Bitmap, imageName: String): String {
        val context = getApplication<Application>().applicationContext
        val file = File(context.filesDir, imageName)

        return try {
            FileOutputStream(file).use { fos ->
                // Pastikan bitmap tidak null dan dapat dikompres
                if (!bitmap.isRecycled) {
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
                        imageName // Mengembalikan nama file jika sukses
                    } else {
                        Log.e("SaveImage", "Failed to compress bitmap")
                        ""
                    }
                } else {
                    Log.e("SaveImage", "Bitmap is recycled")
                    ""
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("SaveImage", "Error saving image: ${e.message}")
            ""
        }
    }
}
