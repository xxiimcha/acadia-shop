package com.example.acadiashop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter
    private val productsList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = intent.getStringExtra("userName")
        val userId = intent.getStringExtra("userId")

        val tvUserName: TextView = findViewById(R.id.tvUserName)
        tvUserName.text = "Hi $userName,"

        val profileIcon: ImageView = findViewById(R.id.profileIcon)
        profileIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val addIcon: ImageView = findViewById(R.id.addIcon)
        addIcon.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val bellIcon: ImageView = findViewById(R.id.notificationIcon)
        bellIcon.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productsAdapter = ProductsAdapter(productsList, this)
        recyclerView.adapter = productsAdapter


        loadProducts(userId)
    }

    private fun loadProducts(userId: String?) {
        if (userId == null) return

        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/app_load_products.php?userId=$userId"
                val response = HttpHandler.makeGetRequest(url)
                val jsonResponse = JSONObject(response)

                if (jsonResponse.getBoolean("success")) {
                    val productsArray: JSONArray = jsonResponse.getJSONArray("products")
                    for (i in 0 until productsArray.length()) {
                        val productObject = productsArray.getJSONObject(i)
                        val product = Product(
                            productObject.getInt("product_id"),
                            productObject.getString("product_name"),
                            productObject.getString("category"),
                            productObject.getDouble("price"),
                            productObject.getString("image"),
                            productObject.getString("description"),
                            productObject.getInt("quantity")
                            // Uncomment the line below if sellerName is included
                            // productObject.getString("sellerName")
                        )
                        productsList.add(product)
                    }
                    runOnUiThread {
                        productsAdapter.notifyDataSetChanged()
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
