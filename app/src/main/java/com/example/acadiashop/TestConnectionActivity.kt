package com.example.acadiashop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class TestConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_connection)

        val btnTestConnection: Button = findViewById(R.id.btnTestConnection)
        val tvConnectionStatus: TextView = findViewById(R.id.tvConnectionStatus)

        btnTestConnection.setOnClickListener {
            Thread {
                val status = testDatabaseConnection()
                runOnUiThread {
                    tvConnectionStatus.text = status
                }
            }.start()
        }
    }

    private fun testDatabaseConnection(): String {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val connection: Connection = DriverManager.getConnection(
                "jdbc:mysql://srv1402.hstgr.io:3306/u646358860_acadiashop",
                "u646358860_acadiashop",
                "d9WiZheZ="
            )
            connection.close()
            "Connection successful!"
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            "JDBC Driver not found!"
        } catch (e: SQLException) {
            e.printStackTrace()
            "Database connection failed!"
        }
    }
}
