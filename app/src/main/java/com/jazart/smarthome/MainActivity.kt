package com.jazart.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jazart.smarthome.di.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


/**
 * Entry point of the application. This class sets up the navigation component and navigates to
 * either home page if the user is logged in, otherwise the login page
 *
 */

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host)
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        nav_view.setupWithNavController(navController)
        bottom_bar.setupWithNavController(navController, config)
        onFabClick()
        navController.navigate(R.id.homeFragment)
    }

    private fun onFabClick() {
        bottomFab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> showBottomSheet()
                R.id.deviceFragment -> showBottomSheet()
                else -> return@setOnClickListener
            }
        }
    }
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    private fun showBottomSheet() {
        val bottomSheet = DeviceCommandBottomSheet()
        bottomSheet.show(supportFragmentManager, null)
    }
}


fun String.isValidPass(): Boolean = length > 7 && this != "0".repeat(8)