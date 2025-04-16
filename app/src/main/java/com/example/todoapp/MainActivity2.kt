package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val savedEmail = sharedPref.getString("loggedInEmail", "")
        if (isLoggedIn && !savedEmail.isNullOrEmpty()) {
            val intent = Intent(this, App::class.java)
            intent.putExtra("email", savedEmail)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java) // your login/signup screen
            startActivity(intent)
        }

        finish() // Close this activity
    }


    }