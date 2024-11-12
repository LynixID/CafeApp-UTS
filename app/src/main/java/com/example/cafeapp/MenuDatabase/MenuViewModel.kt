package com.example.cafeapp.MenuDatabase

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.CafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val menuDao: MenuDAO
    private val allMakans: LiveData<List<Menu>>

    private val _filteredMakans = MutableLiveData<List<Menu>>() // Untuk hasil filter
    val filteredMakans: LiveData<List<Menu>> get() = _filteredMakans

    // Definisi urutan sorting
    enum class SortOrder { A_TO_Z, Z_TO_A }

    init {
        val db = CafeDatabase.getInstance(application)
        menuDao = db.menuDao()
        allMakans = menuDao.getAll() // Mengambil semua data menu

        // Observe allMakans untuk selalu update _filteredMakans
        allMakans.observeForever { makans ->
            _filteredMakans.value = makans
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Hapus observer dari allMakans untuk menghindari kebocoran memori
        allMakans.removeObserver { _filteredMakans.value = it }
    }

    // Fungsi untuk mendapatkan semua makanan (allMakans) tanpa filter
    fun getAllMakans(): LiveData<List<Menu>> = allMakans

    // Fungsi untuk memuat semua item tanpa filter
    fun loadAllItems() {
        _filteredMakans.value = allMakans.value // Mengambil data dari allMakans dan memperbarui _filteredMakans
    }


    // Fungsi untuk pencarian item
    fun searchItems(query: String) {
        val filteredList = allMakans.value?.filter {
            it.nama.contains(query, ignoreCase = true)
        } ?: emptyList()
        _filteredMakans.value = filteredList
    }

    // Fungsi untuk pengurutan item A-Z atau Z-A
    fun sortItems(order: SortOrder) {
        val sortedList = when (order) {
            SortOrder.A_TO_Z -> _filteredMakans.value?.sortedBy { it.nama }
            SortOrder.Z_TO_A -> _filteredMakans.value?.sortedByDescending { it.nama }
        } ?: emptyList()
        _filteredMakans.value = sortedList
    }

    // Fungsi untuk menyimpan gambar ke penyimpanan internal
    fun saveImageToInternalStorage(bitmap: Bitmap, imageName: String): String? {
        val context = getApplication<Application>().applicationContext
        val directory = File(context.filesDir, "app_images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$imageName.jpg")
        return try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            file.name
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Fungsi untuk memasukkan item menu ke dalam database
    fun insertMakan(menu: Menu) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.insert(menu)
        }
    }

    // Fungsi untuk menghapus item menu berdasarkan ID
    fun deleteMakanById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.deleteById(id)
        }
    }

    // Fungsi untuk mendapatkan makanan berdasarkan ID
    fun getMakanById(id: Int): LiveData<Menu> {
        return menuDao.getMakanById(id) // Metode ini perlu ditambahkan di DAO
    }

    // Fungsi untuk memperbarui data Makan
    fun updateMakan(id: Int, name: String, harga: Int, deskripsi: String, namaFoto: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.updateMakan(id, name, harga, deskripsi, namaFoto)
        }
    }
}
