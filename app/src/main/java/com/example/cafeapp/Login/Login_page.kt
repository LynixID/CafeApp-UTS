package com.example.cafeapp.Login

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.AdminMenu.TambahMenu
import com.example.cafeapp.Home.MainActivity
import com.example.cafeapp.UserActivity
import com.example.cafeapp.UserDatabase.CafeDatabase
import com.example.cafeapp.databinding.LoginPageBinding // Import your generated binding class
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class Login_page : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val usersRef = database.getReference("user")

        binding.BtnSubmit.setOnClickListener {
            val inputUser = binding.inputUser.text.toString()
            val inputPass = binding.inputPassword.text.toString()

            if (inputUser.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan username dan password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var loginSuccess = false
                    for (userSnapshot in snapshot.children) {
                        val username = userSnapshot.child("username").getValue(String::class.java)
                        val password = userSnapshot.child("password").getValue(String::class.java)
                        val role = userSnapshot.child("role").getValue(String::class.java)

                        if (username == inputUser && password == inputPass) {
                            loginSuccess = true
                            when (role) {
                                "admin" -> {
                                    val intent = Intent(this@Login_page, TambahMenu::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                                "staff" -> {
                                    val intent = Intent(this@Login_page, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                                else -> {
                                    Toast.makeText(
                                        this@Login_page,
                                        "Role tidak dikenal",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            break
                        }
                    }
                    if (!loginSuccess) {
                        Toast.makeText(
                            this@Login_page,
                            "Gagal, username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@Login_page,
                        "Gagal memuat data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }
        binding.BtnDaftar.setOnClickListener{
            val intent = Intent(this@Login_page, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
