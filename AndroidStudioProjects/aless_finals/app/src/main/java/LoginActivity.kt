package com.example.aless_finals

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.et_login_username)
        val etPassword = findViewById<EditText>(R.id.et_login_password)
        val btnLogin = findViewById<Button>(R.id.btn_submit_login)
        val cbShowPassword = findViewById<CheckBox>(R.id.cbShowPassword)

        // Pre-fill from registration
        val registeredUsername = intent.getStringExtra("REGISTERED_USERNAME") ?: ""
        val registeredPassword = intent.getStringExtra("REGISTERED_PASSWORD") ?: ""
        etUsername.setText(registeredUsername)
        etPassword.setText(registeredPassword)

        // Toggle password visibility
        cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.transformationMethod = null
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Check login logic
            if (!db.isUsernameExist(username)) {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
            } else if (!db.checkUser(username, password)) {
                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UserListActivity::class.java)
                intent.putExtra("LOGGED_USERNAME", username)
                startActivity(intent)
                finish()
            }
        }
    }
}
