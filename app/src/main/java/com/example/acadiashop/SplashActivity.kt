package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("SplashActivity", "onCreate called")

        // Navigate to SignInActivity after a delay or immediately for testing
        startActivity(Intent(this, SignInActivity::class.java))
        Log.d("SplashActivity", "Navigating to SignInActivity")
        finish()
    }
}
