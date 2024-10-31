package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.UserDatabase.CafeDatabase
import com.example.cafeapp.UserDatabase.User
import com.example.cafeapp.UserDatabase.UserDao
import com.example.cafeapp.databinding.ActivityUserBinding
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var userDao: UserDao
    private var usersList: MutableList<User> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedUser: User? = null // Variable to keep track of selected user for editing

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
            if (selectedUser != null) {
                updateUser()
            } else {
                addUser()
            }
        }

        binding.btnBacktoLogin.setOnClickListener {
            val intent = Intent(this, Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.list.setOnItemClickListener { _, _, position, _ ->
            selectedUser = usersList[position]
            binding.user.setText(selectedUser?.username)
            binding.pw.setText(selectedUser?.password)
            binding.role.setSelection(if (selectedUser?.role == "admin") 1 else 0)
        }

        binding.list.setOnItemLongClickListener { _, _, position, _ ->
            val userToDelete = usersList[position]
            deleteUser(userToDelete)
            true
        }

        loadUsers()
    }

    private fun addUser() {
        val username = binding.user.text.toString()
        val password = binding.pw.text.toString()
        val role = binding.role.selectedItem.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(username = username, password = password, role = role)
        lifecycleScope.launch {
            userDao.insert(user)
            loadUsers()
            clearInputs()
            Toast.makeText(this@UserActivity, "User added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUser() {
        selectedUser?.let { user ->
            val updatedUsername = binding.user.text.toString()
            val updatedPassword = binding.pw.text.toString()
            val updatedRole = binding.role.selectedItem.toString()

            if (updatedUsername.isEmpty() || updatedPassword.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return
            }

            // Update the user
            val updatedUser = user.copy(username = updatedUsername, password = updatedPassword, role = updatedRole)
            lifecycleScope.launch {
                userDao.update(updatedUser)
                loadUsers()
                clearInputs()
                Toast.makeText(this@UserActivity, "User updated", Toast.LENGTH_SHORT).show()
                selectedUser = null // Clear the selected user after updating
            }
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            userDao.delete(user)
            loadUsers()
            Toast.makeText(this@UserActivity, "User deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            usersList.clear()
            usersList.addAll(userDao.getAllUsers()) // Load all users from the database

            val userNames = usersList.map { "${it.username} (${it.role})" }
            adapter = ArrayAdapter(this@UserActivity, android.R.layout.simple_list_item_1, userNames)
            binding.list.adapter = adapter
        }
    }

    private fun clearInputs() {
        binding.user.text.clear()
        binding.pw.text.clear()
        binding.role.setSelection(0)
    }
}
