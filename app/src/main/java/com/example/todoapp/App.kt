package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityAppBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this)
        binding.notesRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerview.adapter = notesAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteACtivity::class.java)
            startActivity(intent)
        }

    }
    override fun onResume(){
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())

    }
}










