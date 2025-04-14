package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivityProfileinfoBinding


class profileinfo : AppCompatActivity() {
    private lateinit var binding: ActivityProfileinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email") ?: ""
        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.getUserByEmail(email)
        if(user != null){
            binding.editTextfirstname.setText(user.firstName)
            binding.editTextlastname.setText(user.lastName)
            binding.editTextEmail.setText(user.email)
            binding.editTextPhone.setText(user.phoneNumber)
            binding.editTextPassword.setText(user.password)
        }else{
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
        binding.editTextfirstname.isEnabled = true
        binding.editTextlastname.isEnabled = true
        binding.editTextEmail.isEnabled = true
        binding.editTextPhone.isEnabled = true
        binding.editTextPassword.isEnabled = true

        binding.buttonUpdate.setOnClickListener {
            val updatedUser = User(
                binding.editTextfirstname.text.toString(),
                binding.editTextlastname.text.toString(),
                binding.editTextPhone.text.toString(),
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            )

            val dbHelper = DatabaseHelper(this)
            val result = dbHelper.updateUser(updatedUser)

            if (result > 0) {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.logoutButton.setOnClickListener {
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply() // Clears isLoggedIn and email

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }



    }




    }