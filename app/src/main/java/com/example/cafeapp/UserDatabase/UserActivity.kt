package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.UserDatabase.CafeDatabase
import com.example.cafeapp.UserDatabase.User
import com.example.cafeapp.UserDatabase.UserDB
import com.example.cafeapp.UserDatabase.UserDao
import com.example.cafeapp.databinding.ActivityUserBinding
import com.google.common.base.Objects
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var userDao: UserDao
    private var usersList: MutableList<UserDB> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedUser: UserDB? = null  // Variable to keep track of selected user for editing
    private lateinit var selectedUserKey: String

//    Inisialisasi Firebase
    val database = Firebase.database
    val myRef = database.getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        val db = CafeDatabase.getInstance(applicationContext)
        userDao = db.userDao()

        val roles = arrayOf("staff", "admin")
        binding.role.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        binding.add.setOnClickListener {
            val username = binding.user.text.toString()
            val password = binding.pw.text.toString()
            val role = binding.role.selectedItem.toString()

            // Validasi input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jika selectedUser null, tambahkan data baru
            addUserToDB(username, password, role)
        }

        binding.update.setOnClickListener{
            val username = binding.user.text.toString()
            val password = binding.pw.text.toString()
            val role = binding.role.selectedItem.toString()


            val updatedData = mapOf(
                "username" to username,
                "password" to password,
                "role" to role,
                "key" to selectedUserKey
            )

            val database = Firebase.database
            val myRef = database.getReference("user")


            myRef.child(selectedUserKey).setValue(updatedData)
                .addOnSuccessListener {
                    clearInputs()
                    Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                    selectedUser = null // Reset setelah update
                    loadUsersDB()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update user: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                binding.add.visibility = View.VISIBLE
                binding.update.visibility = View.GONE

        }

        binding.btnBacktoLogin.setOnClickListener {
            val intent = Intent(this, Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.list.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = usersList[position] // Ambil pengguna yang dipilih berdasarkan posisi
            binding.user.setText(selectedUser.username) // Tampilkan username di EditText
            binding.pw.setText(selectedUser.password)   // Tampilkan password di EditText

            // Pilih item pada spinner berdasarkan peran
            val roleIndex = if (selectedUser.role == "admin") 1 else 0
            binding.role.setSelection(roleIndex)

            binding.add.visibility = View.GONE
            binding.update.visibility = View.VISIBLE

            Log.d("selecteduser", selectedUser.key)
            selectedUserKey = selectedUser.key
        }

        binding.list.setOnItemLongClickListener { _, _, position, _ ->
            val userToDelete = usersList[position] // Ambil data pengguna berdasarkan posisi

            // Tampilkan popup konfirmasi sebelum menghapus
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi Penghapusan")
                setMessage("Apakah Anda yakin ingin menghapus pengguna ${userToDelete.username}?")
                setPositiveButton("Ya") { dialog, _ ->
                    // Jika pengguna menekan "Ya", hapus data
                    deleteUserFromDB(userToDelete.key)
                    usersList.removeAt(position) // Hapus pengguna dari daftar lokal
                    adapter.notifyDataSetChanged() // Perbarui tampilan ListView
                    dialog.dismiss()
                    Toast.makeText(this@UserActivity, "Pengguna berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this@UserActivity, "Penghapusan dibatalkan", Toast.LENGTH_SHORT).show()
                }
            }.show()

            true
        }


        loadUsersDB()
    }


    private fun addUserToDB(username: String, password: String, role: String ) {

//        Create Hash
        val userHashMap = HashMap<String, Any>()
        userHashMap["username"] = username
        userHashMap["password"] = password
        userHashMap["role"] = role

//        Instantiate Database
        val key = myRef.push().key!! // Gunakan `!!` jika yakin tidak akan null
        userHashMap["key"] = key

        myRef.child(key).setValue(userHashMap).addOnSuccessListener {
            Toast.makeText(this, "Ditambahkan", Toast.LENGTH_SHORT).show()
            loadUsersDB()
        }
    }

    private fun deleteUserFromDB(key:String) {

        // Menghapus data berdasarkan key
        myRef.child(key).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Pengguna berhasil dihapus", Toast.LENGTH_SHORT).show()
                loadUsersDB()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal menghapus pengguna: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUsersDB() {
        // Kosongkan list sebelum memuat data baru
        usersList.clear()

        // Membaca data dari Firebase
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserDB::class.java)
                    if (user != null) {
                        usersList.add(user)
                    }
                }

                // Memperbarui ListView dengan data terbaru
                val userNames = usersList.map { "${it.username} (${it.role})" }
                adapter = ArrayAdapter(this@UserActivity, android.R.layout.simple_list_item_1, userNames)
                binding.list.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserActivity, "Gagal memuat pengguna: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearInputs() {
        binding.user.text.clear()
        binding.pw.text.clear()
        binding.role.setSelection(0)
    }
}

//    private fun loadUsers() {
//        lifecycleScope.launch {
//            usersList.clear()
//            usersList.addAll(userDao.getAllUsers()) // Load all users from the database
//
//            val userNames = usersList.map { "${it.username} (${it.role})" }
//            adapter = ArrayAdapter(this@UserActivity, android.R.layout.simple_list_item_1, userNames)
//            binding.list.adapter = adapter
//        }
//    }

//private fun addUser() {
//        val username = binding.user.text.toString()
//        val password = binding.pw.text.toString()
//        val role = binding.role.selectedItem.toString()
//
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val user = User(username = username, password = password, role = role)
//        lifecycleScope.launch {
//            userDao.insert(user)
//            loadUsersDB()
//            clearInputs()
//            Toast.makeText(this@UserActivity, "User added", Toast.LENGTH_SHORT).show()
//
////            Masuk ke Firebase
//            addUserToDB(username, password, role)
//        }
//    }
