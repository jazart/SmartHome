package com.jazart.smarthome.common

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
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
import com.jazart.smarthome.devicemgmt.setVisible
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

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, ConfirmDialog.OnDialogClicked {

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
        savedInstanceState?.let { bundle ->
            navController.restoreState(bundle.getBundle(NAV_STACK))
            setupBaseUi()
            return
        }
        if (getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).getString(JWT, null).isNullOrBlank()) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
            usernameHeader?.text = getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).getString(USERNAME, "")
        } else {
            navController.navigate(R.id.homeFragment)
        }
        setupBaseUi()
    }

    override fun onOptionClicked(option: Int) {
        getSharedPreferences(USER_JWT, Context.MODE_PRIVATE).edit().clear().apply()
        finishAndRemoveTask()
    }

    private fun setupBaseUi() {
        sharedUiViewModel.highlightIcon.observe(this, Observer { event ->
            event.consume()?.let { updateToolbar(it) }
        })
        sharedUiViewModel.poorConnectionView.observe(this, Observer { event ->
            val message = event.consume()
            if (message == null) {
                poorConnectionTv.visibility = View.INVISIBLE
                return@Observer
            }
            poorConnectionTv.text = message
            if (!poorConnectionTv.isVisible) {
                animateBanner()
            }
        })

        logoutBtn.setOnClickListener {
            ConfirmDialog.newInstance("Are you sure you want to logout?").apply {
                targetFrag = this@MainActivity
                show(supportFragmentManager, null)
            }
        }
    }

    private fun animateBanner() {
        poorConnectionTv.measure(MATCH_PARENT, WRAP_CONTENT)
        val originalHeight = poorConnectionTv.height + poorConnectionTv.paddingTop + poorConnectionTv.paddingBottom
        (poorConnectionTv.layoutParams as CoordinatorLayout.LayoutParams).apply {
            height = 0
            poorConnectionTv.requestLayout()
        }
        poorConnectionTv.setVisible()
//        poorConnectionTv.animate().scaleY(200f).setDuration(1000L).start()
        ValueAnimator.ofInt(0, originalHeight * 2).apply {
            duration = 400L
            interpolator = OvershootInterpolator()
            addUpdateListener { anim ->
                poorConnectionTv.layoutParams =
                    (poorConnectionTv.layoutParams as CoordinatorLayout.LayoutParams).apply {
                        height = anim.animatedValue as Int
                        poorConnectionTv.requestLayout()
                    }
            }
            start()
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
                    updateBottomBarVisibility(true)
                }
                destination.id == R.id.deviceFragment -> {
                    updateBottomBarVisibility()
                    bottom_bar.navigationIcon = null
                    bottom_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    bottom_bar.replaceMenu(R.menu.device_menu)
                    bottom_bar.invalidate()
                    bottomFab.setImageDrawable(getDrawable(R.drawable.ic_device_black_24dp))
                }
                destination.id == R.id.homeFragment -> {
                    updateBottomBarVisibility()
                    bottom_bar.navigationIcon = getDrawable(R.drawable.ic_menu_white_24dp)
                    bottom_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    bottom_bar.replaceMenu(R.menu.menu)
                    bottomFab.setImageDrawable(getDrawable(R.drawable.ic_add_black_24dp))
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

    private fun updateBottomBarVisibility(shouldHide: Boolean = false) {
        if(shouldHide) {
            bottom_bar.setGone()
            bottomFab.setGone()
        } else {
            bottomFab.setVisible()
            bottom_bar.setVisible()
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
            putBundle(NAV_STACK, navController.saveState())
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
        const val USER_JWT = "user_jwt"
        const val USERNAME = "username"
        const val JWT = "jwt"
        const val NAV_STACK = "navigation stack"
    }
}