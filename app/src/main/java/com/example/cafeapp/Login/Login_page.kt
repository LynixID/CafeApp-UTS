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
import com.example.cafeapp.UserDatabase.UserActivity
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


    private lateinit var db: CafeDatabase
    private lateinit var binding: LoginPageBinding // Declare the binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Initialize the database
        db = CafeDatabase.getInstance(this)

        binding.BtnSubmit.setOnClickListener {
            val inputUser = binding.inputUser.text.toString()
            val inputPass = binding.inputPassword.text.toString()

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

        // Logic for the register button
        binding.BtnDaftar.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                Log.d("Firebase", "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }

        })
    }
}
