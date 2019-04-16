package com.jazart.smarthome.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.jazart.smarthome.R
import com.jazart.smarthome.devicemgmt.getViewModel
import com.jazart.smarthome.devicemgmt.setGone
import com.jazart.smarthome.di.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
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
    private lateinit var sharedUiViewModel: SharedUiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedUiViewModel = getViewModel(viewModelFactory)
        navController = findNavController(R.id.nav_host)
        setupNavigation()
        onFabClick()
        savedInstanceState?.let {
            navController.navigate(it.getInt(LOCATION))
            return
        }
        if (getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).getString(JWT, null).isNullOrBlank()) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
            usernameHeader?.text = getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).getString(USERNAME, "")
        } else {
            navController.navigate(R.id.homeFragment)
        }
        sharedUiViewModel.highlightIcon.observe(this, Observer { event ->
            event.consume()?.let { updateToolbar(it) }
        })
        setupBaseUi()
    }

    private fun setupBaseUi() {
        logoutBtn.setOnClickListener {
            getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).edit().clear().apply()
            finishAndRemoveTask()
        }
    }

    private fun setupNavigation() {
        val config = AppBarConfiguration(navController.graph, drawer_layout)
        nav_view.setupWithNavController(navController)
        bottom_bar.setupWithNavController(navController, config)
        bottom_bar.replaceMenu(R.menu.menu)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when {
                destination.id == R.id.loginFragment || destination.id == R.id.signupFragment -> {
                    bottomFab.setGone()
                    bottom_bar.setGone()
                }
                destination.id == R.id.deviceFragment -> {
                    bottom_bar.navigationIcon = null
                    bottom_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    bottom_bar.replaceMenu(R.menu.device_menu)
                    bottom_bar.invalidate()
                    bottomFab.setImageDrawable(getDrawable(R.drawable.ic_device_black_24dp))
                }
                destination.id == R.id.homeFragment -> {
                    bottom_bar.navigationIcon = getDrawable(R.drawable.ic_menu_white_24dp)
                    bottom_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    bottom_bar.replaceMenu(R.menu.menu)
                    bottomFab.setImageDrawable(getDrawable(R.drawable.ic_add_black_24dp))
                }
                else -> {
                    bottomFab.visibility = View.VISIBLE
                    bottom_bar.visibility = View.VISIBLE
                }
            }
        }
        bottom_bar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.scheduleCommand -> return@setOnMenuItemClickListener true
                R.id.removeDevice -> sharedUiViewModel.onMenuClicked(item.itemId)
                R.id.setFavorite -> sharedUiViewModel.onMenuClicked(item.itemId)
                else -> return@setOnMenuItemClickListener false
            }
        }
    }

    private fun onFabClick() {
        bottomFab.setOnClickListener {
            navController.currentDestination?.let { dest ->
                sharedUiViewModel.onBottomFabClicked(dest.id)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            navController.currentDestination?.id?.let { putInt(LOCATION, it) }
        })
    }

    private fun updateToolbar(data: Pair<Boolean, Int>) {
        when (data.second) {
            R.id.setFavorite -> {
                if (data.first) {
                    bottom_bar.menu.getItem(2).icon.setTint(getColor(R.color.colorAccent))
                } else {
                    bottom_bar.menu.getItem(2).icon.setTint(getColor(android.R.color.white))
                }
                bottom_bar.invalidate()
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    companion object {
        const val TAG = "MainActivity"
        const val LOCATION = "Current Location"
        const val USER_JWT = "user_jwt"
        const val USERNAME = "username"
        const val JWT = "jwt"
    }
}