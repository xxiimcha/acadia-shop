package com.example.acadiashop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnLogOut: Button = findViewById(R.id.btnLogOut)
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val tvViewSelfie: TextView = findViewById(R.id.tvViewSelfie)

        // Set profile data (you can get these values from your database or intent)
        val tvName: TextView = findViewById(R.id.tvName)
        val tvStudentId: TextView = findViewById(R.id.tvStudentId)
        val tvMobileNo: TextView = findViewById(R.id.tvMobileNo)
        val tvSchoolEmail: TextView = findViewById(R.id.tvSchoolEmail)
        val tvDepartment: TextView = findViewById(R.id.tvDepartment)

        // Example values, replace with actual data
        tvName.text = "Lorem Bartolome"
        tvStudentId.text = "2022-123456"
        tvMobileNo.text = "+63 912 453 7890"
        tvSchoolEmail.text = "loremb@school.com"
        tvDepartment.text = "SECA – School of Engineering, Computing, and Architecture"

        btnLogOut.setOnClickListener {
            // Handle logout
            finish()
        }

        profileImage.setOnClickListener {
            // Handle view selfie click
        }

        tvViewSelfie.setOnClickListener {
            // Handle view selfie click
        }
    }
}
