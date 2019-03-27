package com.jazart.smarthome.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jazart.smarthome.R
import com.jazart.smarthome.devicemgmt.getViewModel
import com.jazart.smarthome.devicemgmt.setGone
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
    private lateinit var fabViewModel: FabViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fabViewModel = getViewModel(viewModelFactory)
        navController = findNavController(R.id.nav_host)
        setupNavigation()
        onFabClick()
        savedInstanceState?.let {
            navController.navigate(it.getInt(LOCATION))
            return
        }
        if (getSharedPreferences("user_jwt", Context.MODE_PRIVATE).getString("jwt", null).isNullOrBlank()) {
            navController.navigateSafely(R.id.action_homeFragment_to_loginFragment, TAG)
        } else {
            navController.navigate(R.id.homeFragment)
        }
    }

    private fun setupNavigation() {
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        nav_view.setupWithNavController(navController)
        bottom_bar.setupWithNavController(navController, config)
        bottom_bar.replaceMenu(R.menu.menu)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.signupFragment) {
                bottomFab.setGone()
                bottom_bar.setGone()
            } else {
                bottomFab.visibility = View.VISIBLE
                bottom_bar.visibility = View.VISIBLE
            }
        }
    }

    private fun onFabClick() {
        bottomFab.setOnClickListener {
            navController.currentDestination?.let { dest ->
                fabViewModel.onBottomFabClicked(dest.id)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            navController.currentDestination?.id?.let { putInt(LOCATION, it) }
        })
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    companion object {
        const val TAG = "MainActivity"
        const val LOCATION = "Current Location"
    }
}

fun NavController.navigateSafely(id: Int, tag: String) {
    try {
        navigate(id)
    } catch (e: IllegalArgumentException) {
        Log.e(tag, e.message, e)
    }
}