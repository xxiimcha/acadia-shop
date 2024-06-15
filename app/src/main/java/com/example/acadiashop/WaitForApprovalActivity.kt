package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView

class WaitForApprovalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_for_approval)

        val btnGoBack: Button = findViewById(R.id.btnGoBack)

        btnGoBack.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
