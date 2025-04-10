package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.todoapp.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int =-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }
        val note = db.getNoteByID(noteId)
        binding.updatetitleEdittext.setText(note.title)
        binding.updatecontentEdittext.setText(note.content)

        binding.updatesavebutton.setOnClickListener{
            val newtitle = binding.updatetitleEdittext.text.toString()
            val newcontent = binding.updatecontentEdittext.text.toString()
            val updateNote= Note(noteId,newtitle,newcontent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this,"Note Edited", Toast.LENGTH_SHORT).show()
        }
    }
}