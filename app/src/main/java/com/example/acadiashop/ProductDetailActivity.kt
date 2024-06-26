package com.example.acadiashop

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var meetupRecyclerView: RecyclerView
    private lateinit var meetupAdapter: MeetupAdapter
    private lateinit var spinnerMeetup: Spinner
    private val meetupList = mutableListOf<Meetup>()
    private val meetupOptions = mutableListOf<String>()
    private var productId: Int = 0
    private var userId: String? = null
    private var productPrice: Double = 0.0
    private var productName: String? = null
    private var productImage: String? = null
    private var productQuantity: Int = 1
    private var selectedMeetup: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productName = intent.getStringExtra("productName")
        val productCategory = intent.getStringExtra("category")
        productPrice = intent.getDoubleExtra("price", 0.0)
        productImage = intent.getStringExtra("image")
        val productDescription = intent.getStringExtra("description")
        productId = intent.getIntExtra("productId", 0)
        userId = intent.getStringExtra("userId")

        // Log the productId for verification
        Log.d("ProductDetailActivity", "Received productId: $productId")

        val tvProductName: TextView = findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = findViewById(R.id.tvProductPrice)
        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)
        val tvProductDescription: TextView = findViewById(R.id.tvProductDescription)
        val btnReport: ImageView = findViewById(R.id.btnReport)
        val btnOrder: Button = findViewById(R.id.btnOrder)
        val ivMinus: ImageView = findViewById(R.id.ivMinus)
        val ivPlus: ImageView = findViewById(R.id.ivPlus)
        val tvQuantity: TextView = findViewById(R.id.tvQuantity)
        spinnerMeetup = findViewById(R.id.spinnerMeetup)

        tvProductName.text = productName
        tvProductPrice.text = "₱${productPrice}"
        tvProductDescription.text = productDescription
        tvQuantity.text = productQuantity.toString()

        Glide.with(this).load(productImage).into(ivProductImage)

        meetupRecyclerView = findViewById(R.id.meetupRecyclerView)
        meetupRecyclerView.layoutManager = LinearLayoutManager(this)
        meetupAdapter = MeetupAdapter(meetupList)
        meetupRecyclerView.adapter = meetupAdapter

        loadMeetups(productId)

        btnReport.setOnClickListener {
            showReportDialog()
        }

        btnOrder.setOnClickListener {
            showOrderPreviewDialog()
        }

        ivMinus.setOnClickListener {
            if (productQuantity > 1) {
                productQuantity--
                tvQuantity.text = productQuantity.toString()
            }
        }

        ivPlus.setOnClickListener {
            productQuantity++
            tvQuantity.text = productQuantity.toString()
        }

        spinnerMeetup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMeetup = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedMeetup = ""
            }
        }
    }

    private fun loadMeetups(productId: Int) {
        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/app_load_meetups.php?productId=$productId"
                Log.d("ProductDetailActivity", "Requesting URL: $url")
                val response = HttpHandler.makeGetRequest(url)
                Log.d("ProductDetailActivity", "Response: $response")

                if (response.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this, "No response from server", Toast.LENGTH_SHORT).show()
                    }
                    return@thread
                }

                val jsonResponse = JSONObject(response)
                Log.d("ProductDetailActivity", "JSON Response: $jsonResponse")

                if (jsonResponse.getBoolean("success")) {
                    val meetupsArray: JSONArray = jsonResponse.getJSONArray("meetups")
                    for (i in 0 until meetupsArray.length()) {
                        val meetupObject = meetupsArray.getJSONObject(i)
                        val meetup = Meetup(
                            meetupObject.getInt("meet_id"),
                            meetupObject.getInt("user_id"),
                            meetupObject.getInt("product_id"),
                            meetupObject.getString("meet_place"),
                            meetupObject.getString("meet_time"),
                            meetupObject.getString("date_created")
                        )
                        meetupList.add(meetup)
                        meetupOptions.add("${meetup.meet_place} at ${meetup.meet_time}")
                    }
                    runOnUiThread {
                        meetupAdapter.notifyDataSetChanged()
                        val meetupAdapter = ArrayAdapter(this@ProductDetailActivity, android.R.layout.simple_spinner_item, meetupOptions)
                        meetupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerMeetup.adapter = meetupAdapter
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

    private fun showReportDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_report, null)
        val spinnerReportReason: Spinner = dialogView.findViewById(R.id.spinnerReportReason)
        val btnSubmitReport: Button = dialogView.findViewById(R.id.btnSubmitReport)

        val reasons = arrayOf("It's spam", "It's scam", "It's inappropriate", "It's suspicious")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, reasons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerReportReason.adapter = adapter

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnSubmitReport.setOnClickListener {
            val selectedReason = spinnerReportReason.selectedItem.toString()
            submitReport(selectedReason)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun submitReport(reason: String) {
        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/app_submit_report.php"
                val postData = "user_id=$userId&product_id=$productId&reason=$reason"
                Log.d("ProductDetailActivity", "Submitting report with data: $postData")
                val response = HttpHandler.makePostRequest(url, postData)
                Log.d("ProductDetailActivity", "Report response: $response")

                if (response.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this, "No response from server", Toast.LENGTH_SHORT).show()
                    }
                    return@thread
                }

                val jsonResponse = JSONObject(response)

                runOnUiThread {
                    if (jsonResponse.getBoolean("success")) {
                        Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show()
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
    }

    private fun showOrderPreviewDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_order_preview, null)
        val ivOrderPreviewImage: ImageView = dialogView.findViewById(R.id.ivOrderPreviewImage)
        val tvOrderPreviewName: TextView = dialogView.findViewById(R.id.tvOrderPreviewName)
        val tvOrderPreviewPrice: TextView = dialogView.findViewById(R.id.tvOrderPreviewPrice)
        val tvOrderPreviewQuantity: TextView = dialogView.findViewById(R.id.tvOrderPreviewQuantity)
        val tvOrderPreviewMeetup: TextView = dialogView.findViewById(R.id.tvOrderPreviewMeetup)
        val btnConfirmOrder: Button = dialogView.findViewById(R.id.btnConfirmOrder)

        Glide.with(this).load(productImage).into(ivOrderPreviewImage)
        tvOrderPreviewName.text = productName
        tvOrderPreviewPrice.text = "₱${productPrice}"
        tvOrderPreviewQuantity.text = "Quantity: $productQuantity"
        tvOrderPreviewMeetup.text = "Meetup: $selectedMeetup"

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnConfirmOrder.setOnClickListener {
            confirmOrder()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun confirmOrder() {
        thread {
            try {
                val url = "https://acadia-shop.store/scripts/ajax/app_create_order.php"
                val postData = "user_id=$userId&product_id=$productId&quantity=$productQuantity&meetup=$selectedMeetup"
                Log.d("ProductDetailActivity", "Creating order with data: $postData")
                val response = HttpHandler.makePostRequest(url, postData)
                Log.d("ProductDetailActivity", "Order response: $response")

                if (response.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this, "No response from server", Toast.LENGTH_SHORT).show()
                    }
                    return@thread
                }

                val jsonResponse = JSONObject(response)

                runOnUiThread {
                    if (jsonResponse.getBoolean("success")) {
                        Toast.makeText(this, "Order confirmed!", Toast.LENGTH_SHORT).show()
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
    }
}
