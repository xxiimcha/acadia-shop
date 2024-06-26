package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import kotlin.concurrent.thread

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnSignIn: Button = findViewById(R.id.btnSignIn)
        val tvSignUp: TextView = findViewById(R.id.tvSignUp)
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                thread {
                    try {
                        val url = "https://acadia-shop.store/scripts/ajax/ajax_login.php"
                        val postData = "email=$email&password=$password"
                        val response = HttpHandler.makePostRequest(url, postData)
                        runOnUiThread {
                            val jsonResponse = JSONObject(response)
                            if (jsonResponse.getBoolean("success")) {
                                val intent = Intent(this, HomeActivity::class.java)
                                intent.putExtra("userName", jsonResponse.getString("name"))
                                intent.putExtra("userId", jsonResponse.getString("user_id"))
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

        tvSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            // Handle forgot password click
        }
    }
}
