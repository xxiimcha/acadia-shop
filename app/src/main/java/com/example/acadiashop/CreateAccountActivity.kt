package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import kotlin.concurrent.thread

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val etFullName: EditText = findViewById(R.id.etFullName)
        val etMobileNumber: EditText = findViewById(R.id.etMobileNumber)
        val etStudentId: EditText = findViewById(R.id.etStudentId)
        val etSchoolEmail: EditText = findViewById(R.id.etSchoolEmail)
        val etDepartment: EditText = findViewById(R.id.etDepartment)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val mobileNumber = etMobileNumber.text.toString().trim()
            val studentId = etStudentId.text.toString().trim()
            val schoolEmail = etSchoolEmail.text.toString().trim()
            val department = etDepartment.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isNotEmpty() && mobileNumber.isNotEmpty() && studentId.isNotEmpty() &&
                schoolEmail.isNotEmpty() && department.isNotEmpty() && password.isNotEmpty()) {

                thread {
                    try {
                        val url = "https://acadia-shop.store/scripts/ajax/ajax_register.php"
                        val postData = "fullName=$fullName&mobileNumber=$mobileNumber&studentId=$studentId&schoolEmail=$schoolEmail&department=$department&password=$password"
                        val response = HttpHandler.makePostRequest(url, postData)
                        runOnUiThread {
                            val jsonResponse = JSONObject(response)
                            if (jsonResponse.getBoolean("success")) {
                                val intent = Intent(this, VerificationActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
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
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
