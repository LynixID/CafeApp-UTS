package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.TambahMenu.TambahMenu

class Login_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val InputUsername: EditText = findViewById(R.id.inputUser)
        val InputPassword: EditText = findViewById(R.id.inputPassword)
        val TombolSubmit: Button = findViewById(R.id.BtnSubmit)

        // Data login untuk admin dan staff
        val dataLogin = listOf(
            user("staff", "staff"),
            user("admin", "admin")
        )

        TombolSubmit.setOnClickListener {
            val InputUser = InputUsername.text.toString()
            val InputPass = InputPassword.text.toString()

            val user = dataLogin.find { it.username == InputUser && it.password == InputPass }

            if (user != null) {
                if (user.username == "admin") {
                    val intent = Intent(this, TambahMenu::class.java)
                    startActivity(intent)
                    finish()
                }
                else if (user.username == "staff") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Gagal, input data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}