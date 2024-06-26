package com.example.acadiashop

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object HttpHandler {
    @Throws(IOException::class)
    fun makePostRequest(urlString: String, postData: String): String {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.doOutput = true

        val writer = BufferedWriter(OutputStreamWriter(conn.outputStream))
        writer.write(postData)
        writer.flush()
        writer.close()

        val responseCode = conn.responseCode
        val responseMessage = conn.responseMessage
        Log.d("HttpHandler", "POST Response Code: $responseCode")
        Log.d("HttpHandler", "POST Response Message: $responseMessage")

        val inputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
            conn.inputStream
        } else {
            conn.errorStream
        }

        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        Log.d("HttpHandler", "POST Response: $response")
        return response.toString()
    }

    @Throws(IOException::class)
    fun makeGetRequest(urlString: String): String {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        val responseCode = conn.responseCode
        val responseMessage = conn.responseMessage
        Log.d("HttpHandler", "GET Response Code: $responseCode")
        Log.d("HttpHandler", "GET Response Message: $responseMessage")

        val inputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
            conn.inputStream
        } else {
            conn.errorStream
        }

        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        Log.d("HttpHandler", "GET Response: $response")
        return response.toString()
    }
}
