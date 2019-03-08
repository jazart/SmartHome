package com.jazart.smarthome.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.graphql.SignupMutation
import com.jazart.smarthome.R
import com.jazart.smarthome.devicemgmt.DeviceCommandBottomSheet
import com.jazart.smarthome.devicemgmt.HomeFragment
import com.jazart.smarthome.devicemgmt.HomeViewModel
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
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        navController = findNavController(R.id.nav_host)
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        nav_view.setupWithNavController(navController)
        bottom_bar.setupWithNavController(navController, config)
        bottom_bar.replaceMenu(R.menu.menu)
        onFabClick()
//        getSharedPreferences("user_jwt", Context.MODE_PRIVATE).edit().clear().apply()
        if(getSharedPreferences("user_jwt", Context.MODE_PRIVATE).getString("jwt", null).isNullOrBlank()) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
        } else {
            navController.navigate(R.id.homeFragment)
        }
    }

    private fun onFabClick() {
        bottomFab.setOnClickListener {
         navController.currentDestination?.let { dest -> homeViewModel.onBottomFabClicked(dest.id) }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

}