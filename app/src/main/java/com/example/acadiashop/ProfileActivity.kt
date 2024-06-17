package com.example.acadiashop

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import kotlin.concurrent.thread
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnLogOut: Button = findViewById(R.id.btnLogOut)
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val tvViewSelfie: TextView = findViewById(R.id.tvViewSelfie)

        val tvName: TextView = findViewById(R.id.tvName)
        val tvStudentId: TextView = findViewById(R.id.tvStudentId)
        val tvMobileNo: TextView = findViewById(R.id.tvMobileNo)
        val tvSchoolEmail: TextView = findViewById(R.id.tvSchoolEmail)
        val tvDepartment: TextView = findViewById(R.id.tvDepartment)

        val userId = intent.getStringExtra("userId")

        if (userId != null) {
            Log.d("ProfileActivity", "UserId: $userId")
            fetchProfileData(userId, tvName, tvStudentId, tvMobileNo, tvSchoolEmail, tvDepartment)
        } else {
            Log.d("ProfileActivity", "UserId is null")
        }

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

    private fun fetchProfileData(userId: String, tvName: TextView, tvStudentId: TextView, tvMobileNo: TextView, tvSchoolEmail: TextView, tvDepartment: TextView) {
        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/ajax_details.php?user_id=$userId"
                Log.d("ProfileActivity", "Fetching data from URL: $url")
                val response = HttpHandler.makeGetRequest(url)
                Log.d("ProfileActivity", "Response: $response")
                val jsonResponse = JSONObject(response)

                if (jsonResponse.getBoolean("success")) {
                    val data = jsonResponse.getJSONObject("data")

                    runOnUiThread {
                        tvName.text = data.getString("name")
                        tvStudentId.text = data.getString("student_id")
                        tvMobileNo.text = data.getString("phone_number")
                        tvSchoolEmail.text = data.getString("school_email")
                        tvDepartment.text = data.getString("department")
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
