package com.example.aless_finals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserListActivity : AppCompatActivity() {

    private lateinit var recyclerUsers: RecyclerView
    private lateinit var btnLogout: Button
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        recyclerUsers = findViewById(R.id.recyclerUsers)
        btnLogout = findViewById(R.id.btnLogout)
        db = DatabaseHelper(this)

        // Fetch all users
        val userList = db.getAllUsers()

        // Set up RecyclerView
        recyclerUsers.layoutManager = LinearLayoutManager(this)
        recyclerUsers.adapter = UserAdapter(userList)

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
