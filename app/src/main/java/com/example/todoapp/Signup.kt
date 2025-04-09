package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.buttonSignup.setOnClickListener {
            val signupFirstName = binding.editTextfirstname.text.toString().trim()
            val signupLastName = binding.editTextlastname.text.toString().trim()
            val phoneNumber = binding.editTextphonenumberS.text.toString().trim()
            val signupEmail = binding.editTextEmailAddressS.text.toString().trim()
            val signupPassword = binding.editTextPasswordS.text.toString().trim()

            if (validateInputs(signupFirstName, signupLastName, phoneNumber, signupEmail, signupPassword)) {
                signupDatabase(signupFirstName, signupLastName, phoneNumber, signupEmail, signupPassword)
            }
        }
    }

    private fun validateInputs(
        firstname: String,
        lastname: String,
        phonenumber: String,
        email: String,
        password: String
    ): Boolean {
        if (firstname.isEmpty()) {
            binding.editTextfirstname.error = "First name required"
            return false
        }
        if (lastname.isEmpty()) {
            binding.editTextlastname.error = "Last name required"
            return false
        }
        if (phonenumber.isEmpty()) {
            binding.editTextphonenumberS.error = "Phone number required"
            return false
        }
        if (phonenumber.length < 10){
            binding.editTextphonenumberS.error = "Phone number must be 10 digit"
            return false
        }
        if (!android.util.Patterns.PHONE.matcher(phonenumber).matches()) {
            binding.editTextphonenumberS.error = "Invalid phone number"
            return false
        }
        if (email.isEmpty()) {
            binding.editTextEmailAddressS.error = "Email required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmailAddressS.error = "Invalid email"
            return false
        }
        if (password.isEmpty()) {
            binding.editTextPasswordS.error = "Password required"
            return false
        }
        if (password.length < 6) {
            binding.editTextPasswordS.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }

    private fun signupDatabase(
        firstname: String,
        lastname: String,
        phonenumber: String,
        email: String,
        password: String
    ) {
        val insertedRowId = databaseHelper.insertUser(firstname, lastname, phonenumber, email, password)
        if (insertedRowId != -1L) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
