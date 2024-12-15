package com.example.cafeapp.MenuDatabase

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.UserDatabase.CafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// ViewModel untuk mengelola data menu
class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val menuDao: MenuDAO
    private val allMakans: LiveData<List<Menu>>

    private val _filteredMakans = MutableLiveData<List<Menu>>() // Untuk hasil filter
    val filteredMakans: LiveData<List<Menu>> get() = _filteredMakans

    // Definisi urutan sorting
    enum class SortOrder { A_TO_Z, Z_TO_A }

    // Inisialisasi ViewModel dan data awal
    init {
        val db = CafeDatabase.getInstance(application)
        menuDao = db.menuDao()
        allMakans = menuDao.getAll()

        allMakans.observeForever { makans ->
            _filteredMakans.value = makans
        }
    }

    // Membersihkan observer saat ViewModel dihancurkan
    override fun onCleared() {
        super.onCleared()
        allMakans.removeObserver { _filteredMakans.value = it }
    }

    // Fungsi untuk mendapatkan semua makanan
    fun getAllMakans(): LiveData<List<Menu>> = allMakans

    // Fungsi untuk memuat semua item
    fun loadAllItems() {
        _filteredMakans.value = allMakans.value
    }

    // Fungsi untuk pencarian item
    fun searchItems(query: String) {
        val filteredList = allMakans.value?.filter {
            it.nama.contains(query, ignoreCase = true)
        } ?: emptyList()
        _filteredMakans.value = filteredList
    }

    // Fungsi untuk pengurutan item
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

    // Fungsi untuk menghapus item menu berdasarkan ID
    fun deleteMakanById(menuId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.deleteById(menuId)
        }
    }

    // Fungsi untuk memasukkan item menu ke dalam database
    fun insertMakan(menu: Menu, onInsertSuccess: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val generatedId = menuDao.insert(menu) // Mendapatkan _id dari hasil insert
            withContext(Dispatchers.Main) {
                onInsertSuccess(generatedId) // Panggil callback dengan _id
            }
        }
    }

    fun insertUpdateMakan(menu: Menu) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.insert(menu)
        }
    }

    // Fungsi untuk mendapatkan makanan berdasarkan ID
    fun getMakanById(id: Int): LiveData<Menu> {
        return menuDao.getMakanById(id)
    }

    // Fungsi untuk memperbarui data Makan
    fun updateMakan(id: Int, name: String, harga: Int, deskripsi: String, namaFoto: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            menuDao.updateMakan(id, name, harga, deskripsi, namaFoto)
        }
    }
}