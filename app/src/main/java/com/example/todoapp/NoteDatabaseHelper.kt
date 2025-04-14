package com.example.todoapp

import android.adservices.adid.AdId
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, database_name,null,database_version){
    companion object{
        private const val database_name="notesapp.db"
        private const val database_version=1
        private const val table_name="allnotes"
        private const val column_id="id"
        private const val column_title="title"
        private const val column_content="content"
        private const val column_date="date"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "create table $table_name($column_id integer primary key,$column_title text,$column_content text,$column_date text)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val droptableQuery = "drop table if exists $table_name"
        db?.execSQL(droptableQuery)
        onCreate(db)
    }
    fun insertNote(note: Note){
        val  db = writableDatabase
        val values= ContentValues().apply {
            put(column_title,note.title)
            put(column_content,note.content)
            put(column_date, note.date)
        }
        db.insert(table_name,null,values)
        db.close()
    }
    fun getAllNotes(): List<Note>{
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "select * from $table_name"
        val cursor=db.rawQuery(query,null)
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(column_id))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(column_title))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(column_content))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(column_date))
            val note = Note(id,title,content,date)

            notesList.add(note)
        }
        cursor.close()
        db.close()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return notesList.sortedBy {
            try {
                sdf.parse(it.date)
            }catch (e: Exception){
                null
            }

        }
    }
    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(column_title,note.title)
            put(column_content,note.content)
            put(column_date, note.date)
        }
        val whereClause = "$column_id=?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(table_name,values,whereClause,whereArgs)
        db.close()
    }
    fun getNoteByID(noteId: Int):Note{
        val db =readableDatabase
        val query = "SELECT * FROM $table_name ORDER BY $column_date DESC"
        val cursor=db.rawQuery(query,null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(column_id))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(column_title))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(column_content))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(column_date))
        cursor.close()
        db.close()
        return Note(id,title,content,date)
    }
    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereCalause ="$column_id = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(table_name,whereCalause,whereArgs)
        db.close()
    }

}
