package com.jazart.smarthome.devicemgmt

import android.app.Activity
import android.content.Context.WINDOW_SERVICE
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.common.SharedUiViewModel
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_device.*
import javax.inject.Inject

/**
 * This page will display the user's favorite device and its status
 * and also a list of their other devices. There will be a bottom menu bar
 * as well as an option to add another favorite or add/remove a device
 */

class HomeFragment : Fragment(), Injectable, AddDeviceBottomSheet.OnDeviceClickedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var sharedUiViewModel: SharedUiViewModel

    private val clickHandler: (Int, UserQuery.Device) -> Unit = { _, device ->
        deviceViewModel.initCurrentDevice(device)
        findNavController().navigate(
            R.id.action_homeFragment_to_deviceFragment,
            buildBundle(device)
        )
    }

    private val adapter = HomeAdapter(clickHandler)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = getViewModel(viewModelFactory)
        deviceViewModel = getViewModel(viewModelFactory)
        sharedUiViewModel = getViewModel(viewModelFactory)
        homeViewModel.loadDevices()
        observeLiveData()
        updateUi()
    }

    override fun onResume() {
        if (homeViewModel.favoriteDevice.value == null) favDevice.setGone()
        super.onResume()
    }

    override fun onDeviceClicked(device: UserQuery.Device) {
        val snackbar = Snackbar.make(homeFragmentRoot, device.name(), Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        try {
            val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
            params.updateMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + 800)
            snackbarView.layoutParams = params
            snackbar.show()
        } catch (e: ClassCastException) {
            Toast.makeText(requireContext(), "Couldn't raise snackbar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi() {
        home_recyclerView.itemAnimator = DefaultItemAnimator()
        home_recyclerView.adapter = adapter
        home_recyclerView.layoutManager = when {
            isViewInLandscape() ->  {
                GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
            }
            else -> GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        }
        if(isViewInLandscape()) {
            val constraintSet = ConstraintSet().apply { clone(homeFragmentConstraint) }
            constraintSet.clear(favDevice.id, ConstraintSet.END)
            constraintSet.applyTo(homeFragmentConstraint)
            favDevice.layoutParams = (favDevice.layoutParams as ConstraintLayout.LayoutParams).apply {
                width = 1200
                height = 300
            }
            favDevice.animate().apply {
                interpolator = AccelerateDecelerateInterpolator()
                translationX(675f)
                startDelay = 800L
            }.start()
        }
        favDevice.setGone()
        val constraintSet = ConstraintSet()
        constraintSet.clone(deviceItemConstraint)
        constraintSet.clear(deviceImage.id, ConstraintSet.TOP)
        constraintSet.clear(deviceImage.id, ConstraintSet.START)
        constraintSet.clear(deviceName.id, ConstraintSet.END)
        constraintSet.clear(status.id, ConstraintSet.BOTTOM)
        constraintSet.connect(deviceName.id, ConstraintSet.END, deviceItemConstraint.id, ConstraintSet.END)
        constraintSet.connect(deviceImage.id, ConstraintSet.TOP, status.id, ConstraintSet.BOTTOM)
        constraintSet.connect(deviceImage.id, ConstraintSet.START, deviceItemConstraint.id, ConstraintSet.START)
        constraintSet.applyTo(deviceItemConstraint)
        deviceName.layoutParams = (deviceName.layoutParams as ConstraintLayout.LayoutParams).apply {
            width = MATCH_CONSTRAINT
        }
        deviceImage.layoutParams = (deviceImage.layoutParams as ConstraintLayout.LayoutParams).apply {
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            updateMargins(0, 0, 0 , 0)
        }
        deviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
        deviceName.maxLines = 2
        deviceName.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }

    private fun isViewInLandscape(): Boolean {
        val windowManager = requireContext().getSystemService(WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.rotation in listOf(Surface.ROTATION_270, Surface.ROTATION_90)
    }

    private fun observeLiveData() {
        homeViewModel.devices.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
//                displayAddDeviceUi()
            } else {
                adapter.submitList(it)
            }
        })
        homeViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            val span = SpannableString(getString(R.string.home_greeting, user))
            span.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorAccent, null)),
                5,
                span.lastIndexOf(user) + user.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            userGreeting.text = span
        })
        sharedUiViewModel.bottomFabClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { id ->
                if (id == findNavController().currentDestination?.id) {
                    showBottomSheet()
                }
            }
        })

        homeViewModel.favoriteDevice.observe(viewLifecycleOwner, Observer { device ->
            if (device == null) {
                favDevice.setGone()
                favDevice.setOnClickListener(null)
                return@Observer
            }
            deviceName.text = getString(R.string.fav_device, "\n${device.name()}")
            status.text = getString(R.string.status, device.status())
            deviceImage.deviceImage(device.type())
            favDevice.setOnClickListener { clickHandler(0, device) }
            favDevice.setVisible()
        })
    }

    private fun showBottomSheet() {
        AddDeviceBottomSheet().show(childFragmentManager, null)
    }

    companion object {
        const val TAG = "HomeFragment"
        const val ARG_DEVICE_NAME = "ARG_DEVICE_NAME"
        const val ARG_DEVICE_STATUS = "ARG_DEVICE_STATUS"
        const val ARG_DEVICE_OWNER = "ARG_DEVICE_OWNER"
        private const val ARG_DEVICE_COMMANDS = "ARG_DEVICE_COMMANDS"

        fun buildBundle(device: UserQuery.Device): Bundle = Bundle().apply {
            putString(ARG_DEVICE_NAME, device.name())
            putString(ARG_DEVICE_OWNER, device.owner())
            putString(ARG_DEVICE_STATUS, "${device.status()}")
            putStringArrayList(ARG_DEVICE_COMMANDS, device.commands()?.toStringList()?.toCollection(ArrayList()))
        }
    }
}

fun <E> List<E>.toStringList(): List<String> {
    return this.map { obj -> "$obj" }
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(factory: ViewModelFactory): T =
    ViewModelProviders.of(this, factory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getViewModel(factory: ViewModelFactory): T {
    return ViewModelProviders.of(requireActivity(), factory)[T::class.java]
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}