package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val btnNext: Button = findViewById(R.id.btnNext)
        val tvSignIn: TextView = findViewById(R.id.tvSignIn)

        btnNext.setOnClickListener {
            startActivity(Intent(this, VerificationActivity::class.java))
        }

        tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
