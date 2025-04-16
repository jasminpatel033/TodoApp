package com.example.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context):SQLiteOpenHelper(context, database_name,null,
    datbase_version) {
    companion object {
        private const val database_name = "userdatabase.db"
        private const val datbase_version = 1
        private const val table_name = "data"
        private const val column_id = "id"
        private const val column_firstname = "firstname"
        private const val column_Lastname = "Lastname"
        private const val column_phonenumber = "phonenumber"
        private const val column_email = "email"
        private const val column_password = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("create table $table_name(" +
                "$column_id integer primary key autoincrement, " +
                "$column_firstname text," +
                "$column_Lastname text," +
                "$column_phonenumber text," +
                "$column_email text," +
                "$column_password text)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val droptablequery = "Drop Table if exists $table_name"
        db?.execSQL(droptablequery)
        onCreate(db)
    }

    fun insertUser(
        firstname: String,
        Lastname: String,
        phonenumber: String,
        email: String,
        password: String
    ): Long {
        val values = ContentValues().apply {
            put(column_firstname, firstname);
            put(column_Lastname, Lastname);
            put(column_phonenumber, phonenumber);
            put(column_email, email);
            put(column_password, password);

        }
        val db = writableDatabase
        return db.insert(table_name, null, values)
    }

    fun readUserByPhone(phonenumber: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$column_phonenumber = ? AND $column_password = ?"
        val selectionArgs = arrayOf(phonenumber, password)
        val cursor = db.query(table_name, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun readUserByEmail(email: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$column_email = ? AND $column_password = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(table_name, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            "data",
            null,
            "email=?",
            arrayOf(email),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname"))
            val Lastname = cursor.getString(cursor.getColumnIndexOrThrow("Lastname"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phonenumber"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            cursor.close()
            val user = User(
                firstName = firstname,
                lastName = Lastname,
                phoneNumber = phone,
                email = email,
                password = password
            )
            cursor.close()
            user

        } else {
            cursor.close()
            null
        }

    }
    fun getEmailByPhone(phone: String): String {
        val db = readableDatabase
        val query = "SELECT $column_email FROM $table_name WHERE $column_phonenumber = ?"
        val cursor = db.rawQuery(query, arrayOf(phone))
        var email = ""
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(column_email))
        }
        cursor.close()
        db.close()
        return email
    }
    fun updateUser(user: User): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("firstname", user.firstName)
            put("Lastname", user.lastName)
            put("phonenumber", user.phoneNumber)
            put("password", user.password)
        }
        return db.update("data", values, "email = ?", arrayOf(user.email))
    }
    fun deleteUserByEmail(email: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(table_name, "$column_email = ?", arrayOf(email))
        return result > 0
    }



}




