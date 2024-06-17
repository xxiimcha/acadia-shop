package com.example.acadiashop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sellerName: TextView = view.findViewById(R.id.sellerName)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productQuantity: TextView = view.findViewById(R.id.productQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        //holder.sellerName.text = product.sellerName
        holder.productName.text = product.productName
        holder.productPrice.text = "₱${product.price}"
        holder.productQuantity.text = "(${product.quantity}) left"
        // Load image using Glide or another library
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.productImage)
    }

    override fun getItemCount() = productList.size
}


