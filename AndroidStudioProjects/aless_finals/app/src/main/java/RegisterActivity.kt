package com.example.aless_finals

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = DatabaseHelper(this)

        val etFirst = findViewById<EditText>(R.id.et_firstname)
        val etLast = findViewById<EditText>(R.id.et_lastname)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnRegister = findViewById<Button>(R.id.btn_submit_register)
        val cbShowPassword = findViewById<CheckBox>(R.id.cbShowPassword) // Ensure this exists in XML

        // Toggle password visibility
        cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            etPassword.transformationMethod =
                if (isChecked) null else PasswordTransformationMethod.getInstance()
            etPassword.setSelection(etPassword.text.length)
        }

        btnRegister.setOnClickListener {
            val first = etFirst.text.toString().trim()
            val last = etLast.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate empty fields
            if (first.isEmpty() || last.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate names (letters and spaces only)
            val nameRegex = Regex("^[a-zA-Z ]+$")
            if (!first.matches(nameRegex)) {
                Toast.makeText(this, "First name contains invalid characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!last.matches(nameRegex)) {
                Toast.makeText(this, "Last name contains invalid characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if username is already taken
            if (db.isUsernameTaken(username)) {
                Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register user
            val success = db.registerUser(first, last, username, password)
            if (success) {
                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("REGISTERED_USERNAME", username)
                intent.putExtra("REGISTERED_PASSWORD", password)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
