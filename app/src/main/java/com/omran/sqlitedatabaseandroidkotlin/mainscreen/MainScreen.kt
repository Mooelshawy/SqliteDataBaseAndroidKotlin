package com.omran.sqlitedatabaseandroidkotlin.mainscreen

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.collection.arraySetOf
import androidx.navigation.navArgs
import com.omran.sqlitedatabaseandroidkotlin.R
import com.omran.sqlitedatabaseandroidkotlin.databinding.ActivityMainScreenBinding
import com.omran.sqlitedatabaseandroidkotlin.databinding.FragmentLoginBinding
import com.omran.sqlitedatabaseandroidkotlin.model.User
import com.omran.sqlitedatabaseandroidkotlin.ui.login.LoginFragmentDirections
import java.util.zip.Inflater

class MainScreen : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
   private lateinit var  user_name :TextView
    private lateinit  var  user_email :TextView
    //this is not an error
    //using for retrive data from safe args plgin in kotlin
    val args : MainScreenArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)




        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)


        //access navigation drawer header
       val  headerView : View = navView.inflateHeaderView(R.layout.nav_header_main)
        user_name = headerView.findViewById(R.id.header_user_name)
       user_email  =headerView.findViewById(R.id.header_user_email)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            arraySetOf( R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        setUserDataInDrawerHeader()


    }


    fun setUserDataInDrawerHeader(){
        val user = args.userRecord
        user_name.text = user.name
        user_email.text = user.email
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_screen, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}