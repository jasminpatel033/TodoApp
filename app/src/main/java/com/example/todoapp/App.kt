package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityAppBinding


class App : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private var userEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = NoteDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this)
        binding.notesRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerview.adapter = notesAdapter
        userEmail = intent.getStringExtra("email") ?: getSharedPreferences("MyPrefs", MODE_PRIVATE)
            .getString("loggedInEmail", "") ?: ""

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
        binding.profileButton.setOnClickListener {

                val intent = Intent(this, profileinfo::class.java)
                intent.putExtra("email", userEmail )
                startActivity(intent)
            }

        }

    override fun onResume(){
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())

    }
}










