package com.omran.sqlitedatabaseandroidkotlin

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.omran.sqlitedatabaseandroidkotlin.model.User
import com.omran.sqlitedatabaseandroidkotlin.sql.DataBaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var  navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navController = this.findNavController(R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragment)
        return navController.navigateUp()

    }


}