package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cafeapp.TambahMenu.TambahMenu
import com.example.cafeapp.UserDatabase.UserDatabase
import kotlinx.coroutines.launch

class Login_page : AppCompatActivity() {

    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val inputUsername: EditText = findViewById(R.id.inputUser)
        val inputPassword: EditText = findViewById(R.id.inputPassword)
        val tombolSubmit: Button = findViewById(R.id.BtnSubmit)
        val tombolDaftar: Button = findViewById(R.id.BtnDaftar) // Inisialisasi tombol Daftar

        // Initialize the database
        db = UserDatabase.getDatabase(this)

        tombolSubmit.setOnClickListener {
            val inputUser = inputUsername.text.toString()
            val inputPass = inputPassword.text.toString()

            // Check login in the database using a coroutine
            lifecycleScope.launch {
                val user = db.userDao().getUserByUsernameAndPassword(inputUser, inputPass)
                if (user != null) {
                    when (user.role) {
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
                            Toast.makeText(this@Login_page, "Role tidak dikenal", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@Login_page, "Gagal, input data dengan benar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Menambahkan logika untuk tombol Daftar
        tombolDaftar.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java) // Ganti UserActivity sesuai nama kelas yang tepat
            startActivity(intent)
        }
    }
}
