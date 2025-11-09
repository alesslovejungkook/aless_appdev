package com.example.aless_finals

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TweetlyDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COL_ID = "id"
        private const val COL_FIRSTNAME = "firstname"
        private const val COL_LASTNAME = "lastname"
        private const val COL_USERNAME = "username"
        private const val COL_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FIRSTNAME TEXT,
                $COL_LASTNAME TEXT,
                $COL_USERNAME TEXT UNIQUE,
                $COL_PASSWORD TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Check if username exists
    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COL_USERNAME=?",
            arrayOf(username)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ✅ Alias method for backward compatibility
    fun isUsernameExist(username: String): Boolean = isUsernameTaken(username)

    // Register user
    fun registerUser(first: String, last: String, username: String, password: String): Boolean {
        if (isUsernameTaken(username)) return false
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_FIRSTNAME, first)
            put(COL_LASTNAME, last)
            put(COL_USERNAME, username)
            put(COL_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    // Check login credentials
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COL_USERNAME=? AND $COL_PASSWORD=?",
            arrayOf(username, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Get single user
    fun getUserByUsername(username: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COL_USERNAME=?",
            arrayOf(username)
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LASTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD))
            )
        }
        cursor.close()
        return user
    }

    // Get all users (for RecyclerView)
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_LASTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD))
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }
}
