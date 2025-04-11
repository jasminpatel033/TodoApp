package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.todoapp.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var selectedDate: String = ""
    private var noteId: Int =-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDatabaseHelper(this)
        val calendar = Calendar.getInstance()
        binding.dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    selectedDate = sdf.format(calendar.time)
                    binding.dateEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        fun validateInputs(titleText: String, date: String): Boolean {
            if (titleText.isEmpty()) {
                binding.updatetitleEdittext.error = "Title required"
                return false
            }
            if (date.isEmpty()) {
                binding.dateEditText.error = "Date required"
                return false
            }
            return true
        }
        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }
        val note = db.getNoteByID(noteId)
        binding.updatetitleEdittext.setText(note.title)
        binding.updatecontentEdittext.setText(note.content)
        binding.dateEditText.setText(note.date)
        binding.updatesavebutton.setOnClickListener{
            val newtitle = binding.updatetitleEdittext.text.toString()
            val newcontent = binding.updatecontentEdittext.text.toString()
            val newdate = binding.dateEditText.text.toString()
            if(validateInputs(newtitle,selectedDate)) {
                val updateNote = Note(noteId, newtitle, newcontent,newdate)
                db.updateNote(updateNote)
                finish()
                Toast.makeText(this, "Note Edited", Toast.LENGTH_SHORT).show()
            }
        }
    }
}