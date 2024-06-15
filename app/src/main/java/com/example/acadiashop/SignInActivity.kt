package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val btnSignIn: Button = findViewById(R.id.btnSignIn)
        val tvSignUp: TextView = findViewById(R.id.tvSignUp)
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)

        btnSignIn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            // Handle forgot password click
        }
    }
}
