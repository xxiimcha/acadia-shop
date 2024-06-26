package com.example.acadiashop

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductsAdapter(private val products: List<Product>, private val context: Context) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvProductCategory: TextView = itemView.findViewById(R.id.tvProductCategory)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val tvProductDescription: TextView = itemView.findViewById(R.id.tvProductDescription)
        private val tvProductQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        private val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)

        fun bind(product: Product) {
            tvProductName.text = product.productName
            tvProductCategory.text = product.category
            tvProductPrice.text = "₱${product.price}"
            tvProductDescription.text = product.description
            tvProductQuantity.text = "Quantity: ${product.quantity}"
            Glide.with(itemView.context).load(product.image).into(ivProductImage)

            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("productId", product.productId)
                    putExtra("productName", product.productName)
                    putExtra("category", product.category)
                    putExtra("price", product.price)
                    putExtra("image", product.image)
                    putExtra("description", product.description)
                    putExtra("quantity", product.quantity)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size
}
