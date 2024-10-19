//package com.example.cafeapp.MinumDatabase
//
//import android.app.Application
//import android.graphics.Bitmap
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import com.example.cafeapp.Makan
//import com.example.cafeapp.MakanDatabase.MakanDAO
//import com.example.cafeapp.MakanDatabase.MakanDatabase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//class MinumViewModel(application: Application): AndroidViewModel(application) {
//    private val minumDao: MakanDAO
//    private val allMakans: LiveData<List<Makan>>
//
//    init {
//        val db = MakanDatabase.getInstance(application)
//        minumDao = db.menuDao()
//        allMakans = minumDao.getAll() // Mengambil semua data menu
//    }
//
//    fun getAllMinums(): LiveData<List<Makan>> {
//        return allMakans
//    }
//
//    fun insertMinum(menu: Makan) {
//        viewModelScope.launch(Dispatchers.IO) {
//            minumDao.insert(menu)
//        }
//    }
//
//    fun deleteMinumById(id: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            minumDao.deleteById(id)
//        }
//    }
//
//
//    fun saveImageToInternalStorage(bitmap: Bitmap, imageName: String): String? {
//        // Gunakan getApplication() untuk mengakses context
//        val context = getApplication<Application>().applicationContext
//        val directory = File(context.filesDir, "app_images")
//
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//
//        val file = File(directory, "$imageName.jpg")
//        return try {
//            val fos = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//            fos.close()
//            file.name // Mengembalikan nama file
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//}