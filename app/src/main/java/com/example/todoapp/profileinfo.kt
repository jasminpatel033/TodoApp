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

        val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val email = intent.getStringExtra("email") ?: sharedPrefs.getString("loggedInEmail", "") ?: ""




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
            val firstName = binding.editTextfirstname.text.toString().trim()
            val lastName = binding.editTextlastname.text.toString().trim()
            val phone = binding.editTextPhone.text.toString().trim()
            val emailField = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            // Validate fields
            when {
                firstName.isEmpty() -> {
                    binding.editTextfirstname.error = "First name required"
                }
                lastName.isEmpty() -> {
                    binding.editTextlastname.error = "Last name required"
                }
                phone.length != 10 -> {
                    binding.editTextPhone.error = "Enter valid 10-digit phone number"
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(emailField).matches() -> {
                    binding.editTextEmail.error = "Enter valid email"
                }
                password.length < 6 -> {
                    binding.editTextPassword.error = "Password must be at least 6 characters"
                }
                else -> {
                    val updatedUser = User(
                        firstName,
                        lastName,
                        phone,
                        emailField,
                        password
                    )

                    val dbHelper = DatabaseHelper(this)
                    val result = dbHelper.updateUser(updatedUser)

                    if (result > 0) {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            binding.logoutButton.setOnClickListener {
                val db = DatabaseHelper(this)
                val isDeleted = db.deleteUserByEmail(email)
                if (isDeleted) {
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                }

                val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                sharedPref.edit().clear().apply()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

        }



    }




    }