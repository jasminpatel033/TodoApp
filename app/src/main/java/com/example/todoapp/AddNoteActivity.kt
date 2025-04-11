package com.example.todoapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityAddNoteBinding
    private lateinit var db:NoteDatabaseHelper
    private var selectedDate: String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDatabaseHelper(this)
        val calendar = Calendar.getInstance()
        binding.dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    var selectedDate = sdf.format(calendar.time)
                    binding.dateEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.savebutton.setOnClickListener{
            val title = binding.titleEdittext.text.toString()
            val content = binding.contentEdittext.text.toString()
            val selectedDate = " "
            if (validateInputs(title,selectedDate)) {
                val note = Note(0, title, content,selectedDate)
                db.insertNote(note)
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }
    private fun validateInputs(title: String, date: String): Boolean {
        if (title.isEmpty()) {
            binding.titleEdittext.error = "Title required"
            return false
        }
        if (date.isEmpty()) {
            binding.dateEditText.error = "Date required"
            return false
        }
        return true
    }
}