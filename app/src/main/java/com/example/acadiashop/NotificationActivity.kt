package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val userId = intent.getStringExtra("userId")

        if (userId != null) {
            fetchNotifications(userId)
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }

        val homeIcon: ImageView = findViewById(R.id.homeIcon)
        val addIcon: ImageView = findViewById(R.id.addIcon)
        val notificationIcon: ImageView = findViewById(R.id.notificationIcon)

        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        addIcon.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        notificationIcon.setOnClickListener {
            // Handle notification icon click
        }
    }

    private fun fetchNotifications(userId: String) {
        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/ajax_notifications.php?user_id=$userId"
                val response = HttpHandler.makeGetRequest(url)
                runOnUiThread {
                    parseNotifications(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseNotifications(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            if (jsonResponse.getBoolean("success")) {
                val notificationsArray = jsonResponse.getJSONArray("data")
                val notificationList = mutableListOf<Notification>()
                for (i in 0 until notificationsArray.length()) {
                    val notificationObject = notificationsArray.getJSONObject(i)
                    val notification = Notification(
                        text = notificationObject.getString("text"),
                        time = notificationObject.getString("time")
                    )
                    notificationList.add(notification)
                }

                val rvNotifications: RecyclerView = findViewById(R.id.rvNotifications)
                rvNotifications.layoutManager = LinearLayoutManager(this)
                rvNotifications.adapter = NotificationAdapter(notificationList)
            } else {
                Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error parsing notifications", Toast.LENGTH_SHORT).show()
        }
    }
}
