package com.example.acadiashop

data class Product(
    val productId: Int,
    val productName: String,
    val category: String,
    val price: Double,
    val image: String,
    val description: String,
    val quantity: Int
    // Uncomment the line below if sellerName is needed
    // val sellerName: String
)
