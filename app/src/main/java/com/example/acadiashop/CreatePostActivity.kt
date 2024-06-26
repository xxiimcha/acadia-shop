package com.example.acadiashop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread
import org.json.JSONArray
import org.json.JSONObject

class CreatePostActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var ivAttachImage: ImageView
    private lateinit var etItemName: EditText
    private lateinit var etItemDescription: EditText
    private lateinit var llTimePlaceList: LinearLayout
    private lateinit var btnAddTimePlace: Button
    private lateinit var spCategory: Spinner
    private lateinit var btnSubmit: Button

    private val timePlaceList = mutableListOf<TimePlace>()

    data class TimePlace(var time: String, var place: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        ivAttachImage = findViewById(R.id.ivAttachImage)
        etItemName = findViewById(R.id.etItemName)
        etItemDescription = findViewById(R.id.etItemDescription)
        llTimePlaceList = findViewById(R.id.llTimePlaceList)
        btnAddTimePlace = findViewById(R.id.btnAddTimePlace)
        spCategory = findViewById(R.id.spCategory)
        btnSubmit = findViewById(R.id.btnSubmit)

        val userId = intent.getStringExtra("userId")  // Get userId from Intent

        // Set up the spinner with categories
        val categories = arrayOf("Clothes", "Accessories", "Foods")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter

        ivAttachImage.setOnClickListener {
            openImageChooser()
        }

        btnAddTimePlace.setOnClickListener {
            addTimePlaceView()
        }

        btnSubmit.setOnClickListener {
            val itemName = etItemName.text.toString().trim()
            val itemCategory = spCategory.selectedItem.toString()
            val itemDescription = etItemDescription.text.toString().trim()

            if (itemName.isNotEmpty() && itemDescription.isNotEmpty() && imageUri != null && timePlaceList.isNotEmpty()) {
                val imageBase64 = encodeImageToBase64(imageUri!!)
                val timePlaceJson = JSONArray(timePlaceList.map { JSONObject().put("time", it.time).put("place", it.place) }).toString()

                thread {
                    try {
                        val url = "https://acadia-shop.store/scripts/ajax/app_create_post.php"
                        val postData = "userId=$userId&itemName=$itemName&itemCategory=$itemCategory&itemDescription=$itemDescription&itemImages=$imageBase64&timePlace=$timePlaceJson"
                        val response = HttpHandler.makePostRequest(url, postData)
                        val jsonResponse = JSONObject(response)
                        runOnUiThread {
                            if (jsonResponse.getBoolean("success")) {
                                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Please fill in all fields, attach an image, and add at least one time and place.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTimePlaceView() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_time_place, llTimePlaceList, false)

        val etTime: EditText = view.findViewById(R.id.etTime)
        val etPlace: EditText = view.findViewById(R.id.etPlace)
        val btnRemove: Button = view.findViewById(R.id.btnRemoveTimePlace)

        btnRemove.setOnClickListener {
            llTimePlaceList.removeView(view)
            timePlaceList.removeIf { it.time == etTime.text.toString().trim() && it.place == etPlace.text.toString().trim() }
        }

        llTimePlaceList.addView(view)

        timePlaceList.add(TimePlace(etTime.text.toString().trim(), etPlace.text.toString().trim()))
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            ivAttachImage.setImageBitmap(bitmap)
        }
    }

    private fun encodeImageToBase64(imageUri: Uri): String {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
