package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val attachImage: ImageView = findViewById(R.id.ivAttachImage)
        val categorySpinner: Spinner = findViewById(R.id.spCategory)

        // Set up the spinner with categories (assuming categories are predefined in a string array resource)
        val categories = arrayOf("Clothes", "Accessories", "Foods")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        attachImage.setOnClickListener {
            // Handle attach image click
        }

        val homeIcon: ImageView = findViewById(R.id.homeIcon)
        val addIcon: ImageView = findViewById(R.id.addIcon)
        val notificationIcon: ImageView = findViewById(R.id.notificationIcon)

        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        addIcon.setOnClickListener {
            // Handle add icon click
        }

        notificationIcon.setOnClickListener {
            // Handle notification icon click
        }
    }
}
