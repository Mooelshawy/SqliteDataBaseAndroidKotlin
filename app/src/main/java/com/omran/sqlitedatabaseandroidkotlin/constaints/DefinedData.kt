package com.omran.sqlitedatabaseandroidkotlin.constaints

class DefinedData {
    companion object {
        // Database Version
         val DATABASE_VERSION = 1
        // Database Name
         val DATABASE_NAME = "UserManager.db"
        // User table name
         val TABLE_USER = "user"
        // User Table Columns names
         val COLUMN_USER_ID = "user_id"
         val COLUMN_USER_NAME = "user_name"
         val COLUMN_USER_EMAIL = "user_email"
         val COLUMN_USER_PASSWORD = "user_password"
    }
}