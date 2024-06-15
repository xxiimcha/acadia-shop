package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val rvProducts: RecyclerView = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(this)

        // Set up the adapter for RecyclerView (Assuming ProductAdapter is already created)
        // rvProducts.adapter = ProductAdapter(productList)

        val profileIcon: ImageView = findViewById(R.id.profileIcon)
        profileIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Optionally handle other icon clicks
        val homeIcon: ImageView = findViewById(R.id.homeIcon)
        val addIcon: ImageView = findViewById(R.id.addIcon)
        val notificationIcon: ImageView = findViewById(R.id.notificationIcon)

        homeIcon.setOnClickListener {
        }

        addIcon.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        notificationIcon.setOnClickListener {
            // Handle notification icon click
        }
    }
}
