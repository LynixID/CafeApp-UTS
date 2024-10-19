package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.MakanDatabase.TestDatabase1

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val inputUsername: EditText = findViewById(R.id.InputUsername)
        val inputPassword: EditText = findViewById(R.id.InputPassword)
        val tombolSubmit: Button = findViewById(R.id.BtnSubmit)

        tombolSubmit.setOnClickListener {
            val inputUser = inputUsername.text.toString()
            val inputPass = inputPassword.text.toString()

            // Check credentials
            if (inputUser == "admin" && inputPass == "admin") {
                // Navigate to TestDatabase if admin
                val intent = Intent(this, TestDatabase1::class.java)
                startActivity(intent)
            } else if (inputUser == "user" && inputPass == "user") {
                // Navigate to MainActivity if regular user
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                    Toast.makeText(this, "Gagal, input data dengan benar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
