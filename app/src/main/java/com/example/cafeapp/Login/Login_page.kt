package com.example.cafeapp.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.AdminMenu.TambahMenu
import com.example.cafeapp.Home.MainActivity
import com.example.cafeapp.UserActivity
import com.example.cafeapp.databinding.LoginPageBinding // Binding untuk layout login
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// Aktivitas untuk halaman login
class Login_page : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding // Binding untuk elemen tampilan

    // Fungsi utama saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater) // Inisialisasi binding
        setContentView(binding.root)

        val database = Firebase.database // Mengakses database Firebase
        val usersRef = database.getReference("user") // Referensi ke node "user" di Firebase

        // Listener untuk tombol login
        binding.BtnSubmit.setOnClickListener {
            val inputUser = binding.inputUser.text.toString() // Ambil input username
            val inputPass = binding.inputPassword.text.toString() // Ambil input password

            // Validasi input kosong
            if (inputUser.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan username dan password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Periksa username dan password di Firebase
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var loginSuccess = false // Flag untuk status login
                    for (userSnapshot in snapshot.children) {
                        val username = userSnapshot.child("username").getValue(String::class.java) // Ambil username
                        val password = userSnapshot.child("password").getValue(String::class.java) // Ambil password
                        val role = userSnapshot.child("role").getValue(String::class.java) // Ambil role pengguna

                        // Cek kecocokan username dan password
                        if (username == inputUser && password == inputPass) {
                            loginSuccess = true
                            when (role) {
                                "admin" -> {
                                    // Navigasi ke halaman admin
                                    val intent = Intent(this@Login_page, TambahMenu::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                "staff" -> {
                                    // Navigasi ke halaman staff
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
                            break // Keluar dari loop jika login berhasil
                        }
                    }
                    // Tampilkan pesan jika login gagal
                    if (!loginSuccess) {
                        Toast.makeText(
                            this@Login_page,
                            "Gagal, username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Tampilkan pesan jika ada kesalahan saat membaca data Firebase
                    Toast.makeText(
                        this@Login_page,
                        "Gagal memuat data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        // Listener untuk tombol daftar
        binding.BtnDaftar.setOnClickListener {
            val intent = Intent(this@Login_page, UserActivity::class.java) // Navigasi ke halaman pendaftaran
            startActivity(intent)
            finish()
        }
    }
}