package com.example.cafeapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.UserDatabase.UserDao // Ensure proper import
import com.example.cafeapp.UserDatabase.User // Ensure proper import
import com.example.cafeapp.UserDatabase.UserDatabase // Ensure proper import
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao // Corrected variable name
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var buttonAddUser: Button
    private lateinit var listViewUsers: ListView

    private var usersList: MutableList<User> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedUser: User? = null // Variable to keep track of selected user for editing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Initialize database
        val db = UserDatabase.getDatabase(applicationContext)
        userDao = db.userDao()

        editTextUsername = findViewById(R.id.user)
        editTextPassword = findViewById(R.id.pw)
        spinnerRole = findViewById(R.id.role)
        buttonAddUser = findViewById(R.id.add)
        listViewUsers = findViewById(R.id.list)

        val roles = arrayOf("staff", "admin")
        spinnerRole.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        buttonAddUser.setOnClickListener {
            if (selectedUser != null) {
                updateUser()
            } else {
                addUser()
            }
        }

        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            selectedUser = usersList[position]
            editTextUsername.setText(selectedUser?.username)
            editTextPassword.setText(selectedUser?.password)
            spinnerRole.setSelection(if (selectedUser?.role == "admin") 1 else 0)
        }

        listViewUsers.setOnItemLongClickListener { _, _, position, _ ->
            val userToDelete = usersList[position]
            deleteUser(userToDelete)
            true
        }

        loadUsers()
    }

    private fun addUser() {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()
        val role = spinnerRole.selectedItem.toString()

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
            val updatedUsername = editTextUsername.text.toString()
            val updatedPassword = editTextPassword.text.toString()
            val updatedRole = spinnerRole.selectedItem.toString()

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
            usersList.addAll(userDao.getAllUsers()) // Memuat semua user dari database

            val userNames = usersList.map { "${it.username} (${it.role})" }
            adapter = ArrayAdapter(this@UserActivity, android.R.layout.simple_list_item_1, userNames)
            listViewUsers.adapter = adapter
        }
    }

    private fun clearInputs() {
        editTextUsername.text.clear()
        editTextPassword.text.clear()
        spinnerRole.setSelection(0)
    }
}
