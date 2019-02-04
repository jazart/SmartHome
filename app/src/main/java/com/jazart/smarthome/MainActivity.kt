package com.jazart.smarthome

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Entry point of the application. This class sets up the navigation component and navigates to
 * either home page if the user is logged in, otherwise the login page
 *
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host)
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
        nav_view.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, drawer_layout)
        toolbar.setupWithNavController(navController, config)

        navController.navigate(R.id.loginFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


fun String.isValidPass(): Boolean = length > 7 && this != "0".repeat(8)