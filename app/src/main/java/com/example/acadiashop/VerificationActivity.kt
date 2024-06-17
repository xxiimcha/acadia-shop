package com.example.acadiashop

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class VerificationActivity : AppCompatActivity() {

    private lateinit var capturedSelfie: Bitmap
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val btnCaptureSelfie: Button = findViewById(R.id.btnCaptureSelfie)
        val btnSubmitVerification: Button = findViewById(R.id.btnSubmitVerification)

        btnCaptureSelfie.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btnSubmitVerification.setOnClickListener {
            if (::capturedSelfie.isInitialized) {
                thread {
                    try {
                        val url = "https://acadia-shop.store/scripts/ajax/ajax_register.php"
                        val outputStream = ByteArrayOutputStream()
                        capturedSelfie.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        val encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

                        // Retrieve other data from Intent or SharedPreferences
                        val fullName = intent.getStringExtra("fullName")
                        val mobileNumber = intent.getStringExtra("mobileNumber")
                        val studentId = intent.getStringExtra("studentId")
                        val schoolEmail = intent.getStringExtra("schoolEmail")
                        val department = intent.getStringExtra("department")
                        val password = intent.getStringExtra("password")

                        val postData = "fullName=$fullName&mobileNumber=$mobileNumber&studentId=$studentId&schoolEmail=$schoolEmail&department=$department&password=$password&selfie=$encodedImage"
                        val response = HttpHandler.makePostRequest(url, postData)
                        runOnUiThread {
                            val jsonResponse = JSONObject(response)
                            if (jsonResponse.getBoolean("success")) {
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                // Redirect to the login page
                                val intent = Intent(this, SignInActivity::class.java)
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
                Toast.makeText(this, "Please capture a selfie first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            capturedSelfie = imageBitmap
        }
    }
}
