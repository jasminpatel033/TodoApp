package com.example.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivityProfileinfoBinding


class profileinfo : AppCompatActivity() {
    private lateinit var binding: ActivityProfileinfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profileinfo)
        val email = intent.getStringExtra("email") ?: ""
        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.getUserByEmail(email)

        if (user != null) {
            binding.editTextfirstname.setText(user.firstName)
            binding.editTextlastname.setText(user.lastName)
            binding.editTextEmail.setText(user.email)
            binding.editTextPhone.setText(user.phoneNumber)
            binding.editTextPassword.setText(user.password)
        }



    }
}