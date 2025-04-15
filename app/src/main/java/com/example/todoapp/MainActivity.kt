package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)


        // Default: Show phone field, hide email
        binding.editTextphonenumber.visibility = android.view.View.VISIBLE
        binding.editTextEmail.visibility = android.view.View.GONE

        // Handle Tab selection
        binding.PhoneLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Phone selected
                        binding.editTextphonenumber.visibility = android.view.View.VISIBLE
                        binding.editTextEmail.visibility = android.view.View.GONE
                    }
                    1 -> {
                        // Email selected
                        binding.editTextphonenumber.visibility = android.view.View.GONE
                        binding.editTextEmail.visibility = android.view.View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })



        binding.buttonlogin.setOnClickListener {
            val selectedTab = binding.PhoneLayout.selectedTabPosition
            val loginPassword = binding.editTextPassword.text.toString()
            val identifier = if (selectedTab == 0){
                binding.editTextphonenumber.text.toString()
            }else{
                binding.editTextEmail.text.toString()
            }
             loginDatabase(identifier,loginPassword,selectedTab)
        }
        binding.textViewgoforsignup.setOnClickListener {
            val intent = Intent(this,Signup::class.java)
            startActivity(intent)
            finish()
        }


    }
    // Modified login logic based on tab type
    private fun loginDatabase(identifier: String, password: String, tabType: Int) {
        val userExists = if (tabType == 0) {
            // Login using phone
            databaseHelper.readUserByPhone(identifier, password)
        } else {
            // Login using email
            databaseHelper.readUserByEmail(identifier, password)
        }

        if (userExists) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

            val email = if (tabType == 0) {
                // fetch email from database using phone number
                val phone = binding.editTextphonenumber.text.toString().trim()
                databaseHelper.getEmailByPhone(phone)
            } else {
                binding.editTextEmail.text.toString().trim()
            }
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", true)
            editor.putString("loggedInEmail", email) // or use phone if you login via phone
            editor.apply()
            val intent = Intent(this, App::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }
        else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }



    }
}