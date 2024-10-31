package com.example.cafeapp.MinumDatabase

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

class MinumViewModel(application: Application) : AndroidViewModel(application) {
    private val minumDao: MinumDAO
    private val allMinums: LiveData<List<Minum>>

    private val _filteredMinums = MutableLiveData<List<Minum>>() // Untuk hasil filter
    val filteredMinums: LiveData<List<Minum>> get() = _filteredMinums

    // Definisi urutan sorting
    enum class SortOrder { A_TO_Z, Z_TO_A }

    init {
        val db = CafeDatabase.getInstance(application)
        minumDao = db.minumDao()
        allMinums = minumDao.getAll() // Mengambil semua data menu

        // Observe allMinums untuk selalu update filteredMinums
        allMinums.observeForever { minums ->
            _filteredMinums.value = minums
        }
    }

    fun updateMinumList(newList: List<Minum>) {
        _filteredMinums.value = newList
    }

    fun getAllMinums(): LiveData<List<Minum>> {
        return allMinums
    }

    fun insertMinum(menu: Minum) {
        viewModelScope.launch(Dispatchers.IO) {
            minumDao.insert(menu)
        }
    }

    fun deleteMinumById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            minumDao.deleteById(id)
        }
    }

    // Metode untuk mendapatkan minum berdasarkan ID
    fun getMinumById(id: Int): LiveData<Minum> {
        return minumDao.getMinumById(id) // Metode ini perlu ditambahkan di DAO
    }

    // Fungsi untuk update data Minum
    fun updateMinum(id: Int, name: String, harga: Double, deskripsi: String, namaFoto: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            minumDao.updateMinum(id, name, harga, deskripsi, namaFoto)
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
        val filteredList = allMinums.value?.filter {
            it.name.contains(query, ignoreCase = true)
        } ?: emptyList() // Mengembalikan list kosong jika null
        _filteredMinums.value = filteredList
    }

    // Sort items A-Z or Z-A
    fun sortItems(order: SortOrder) {
        val sortedList = when (order) {
            SortOrder.A_TO_Z -> _filteredMinums.value?.sortedBy { it.name }
            SortOrder.Z_TO_A -> _filteredMinums.value?.sortedByDescending { it.name }
        } ?: emptyList()
        _filteredMinums.value = sortedList
    }
}
