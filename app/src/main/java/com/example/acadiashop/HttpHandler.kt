package com.example.acadiashop

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object HttpHandler {
    @Throws(IOException::class)
    fun makePostRequest(url: String, postData: String): String {
        val url = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.doOutput = true

        val writer = BufferedWriter(OutputStreamWriter(conn.outputStream))
        writer.write(postData)
        writer.flush()
        writer.close()

        val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) {
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
        return response.toString()
    }

    @Throws(IOException::class)
    fun makeGetRequest(url: String): String {
        val url = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"

        val inputStream = if (conn.responseCode == HttpURLConnection.HTTP_OK) {
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
        return response.toString()
    }
}
