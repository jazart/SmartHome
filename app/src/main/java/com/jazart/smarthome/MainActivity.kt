package com.jazart.smarthome

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Entry point of the application. This class sets up the navigation component and navigates to
 * either home page if the user is logged in, otherwise the login page
 *
 */

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host)
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        nav_view.setupWithNavController(navController)
        bottom_bar.setupWithNavController(navController, config)
        navController.navigate(R.id.settingsFragment)
    }

    fun onFabClick() {
        bottomFab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> showBottomSheet()
                R.id.deviceFragment -> showBottomSheet()
                else -> return@setOnClickListener
            }
        }
    }

    private fun showBottomSheet() {
        showBottomSheet()

    }


}


fun String.isValidPass(): Boolean = length > 7 && this != "0".repeat(8)