package com.example.cafeapp
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class Login_page : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val InputUsername: EditText = findViewById(R.id.InputUsername)
        val InputPassword: EditText = findViewById(R.id.InputPassword)
        val TombolSubmit: Button = findViewById(R.id.BtnSubmit)

        val Data = datalogin()

        TombolSubmit.setOnClickListener {
            val InputUser = InputUsername.text.toString()
            val InputPass = InputPassword.text.toString()

            if (InputUser == Data.username && InputPass == Data.password) {
                val submit = Intent(this, Login_page::class.java)
                startActivity(submit)
            } else {
                Toast.makeText(this, "Gagal,input data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}