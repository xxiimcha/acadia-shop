package com.example.acadiashop

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseConnection {
    private const val URL = "jdbc:mysql://193.203.184.84:3306/u646358860_acadiashop"
    private const val USER = "u646358860_acadiashop"
    private const val PASSWORD = "d9WiZheZ="

    fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
