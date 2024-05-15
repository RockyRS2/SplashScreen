package com.example.uts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val tvRegister: TextView = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val registeredEmail = sharedPreferences.getString("email", null)
            val registeredPassword = sharedPreferences.getString("password", null)

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Some fields are empty")
            } else if (email == registeredEmail && password == registeredPassword) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Login successful")

                // Start ListActivity
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Invalid credentials")
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}