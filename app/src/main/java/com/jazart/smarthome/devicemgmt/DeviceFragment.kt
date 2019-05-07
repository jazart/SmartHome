package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.common.ConfirmDialog
import com.jazart.smarthome.common.SharedUiViewModel
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import com.jazart.smarthome.util.Event
import kotlinx.android.synthetic.main.fragment_device_detail.*
import javax.inject.Inject

/**
 * Displays a single device and its current status. From here
 * the user can set it as a favorite, or remove it. This is also where
 * the user will be able to send commands to the device.
 */
class DeviceFragment : Fragment(), Injectable, ConfirmDialog.OnDialogClicked {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var deviceViewModel: DeviceViewModel
    lateinit var sharedUiViewModel: SharedUiViewModel
    lateinit var device: UserQuery.Device

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide(Gravity.END)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            deviceName.setText(savedInstanceState.getString(DEVICE_NAME) ?: "")
            deviceStatus.setText(savedInstanceState.getString(DEVICE_STATUS) ?: "")
        }
        deviceViewModel = getViewModel(viewModelFactory)
        sharedUiViewModel = getViewModel(viewModelFactory)
        deviceToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        deviceToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        deviceToolbar.inflateMenu(R.menu.device_menu_top)
        deviceToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.editDevice) deviceViewModel.toggleEdit()
            true
        }
        observeData(deviceViewModel)
    }

    override fun onOptionClicked(option: Int) {
        deviceViewModel.deleteDevice(device)
    }

    private fun observeData(deviceViewModel: DeviceViewModel) {
        sharedUiViewModel.iconClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                when (it) {
                    R.id.removeDevice -> {
                        val dialog = ConfirmDialog.newInstance("Are you sure you want to delete ${device.name()}?")
                        dialog.show(childFragmentManager, null)
                    }
                    R.id.setFavorite -> {
                        deviceViewModel favorite device
                    }
                    else -> return@let
                }
            }
        })

        deviceViewModel.showEditMode.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                if (it) {
                    deviceName.toggleEditableStatus()
                    deviceStatus.toggleEditableStatus()
                } else {
                    deviceName.toggleEditableStatus(it)
                    deviceStatus.toggleEditableStatus(it)
                }
            }
        })

        sharedUiViewModel.bottomFabClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { location ->
                if (location == R.id.deviceFragment) {
                    showBottomSheet()
                }
            }
        })

        deviceViewModel.currentDevice.observe(viewLifecycleOwner, Observer {
            device = it
            deviceImage.deviceImage(device.type())
            sharedUiViewModel.highlightIcon.value = Event(Pair(device.isFavorite, R.id.setFavorite))
            updateUi()
        })

        deviceViewModel.removeDeviceResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { findNavController().navigate(R.id.homeFragment) }
        })

        deviceViewModel.favorite.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                val snackbar =
                    Snackbar.make(devicePageRoot, "${device.name()} added as favorite!", Snackbar.LENGTH_LONG)

                snackbar.view.layoutParams = (snackbar.view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                    anchorId = R.id.snackbarAnchor
                }
                snackbar.show()
            }
        })

        deviceViewModel.commandValue.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { imageUri ->
                showSnackbar()
                streamView.visibility = View.INVISIBLE
                Glide.with(this).applyDefaultRequestOptions(
                    RequestOptions().fitCenter()
                ).load(imageUri).into(cameraImage)
            }
        })
        deviceViewModel.streamValue.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { url ->
                showSnackbar()
                if (streamView != null) {
                    streamView.webViewClient = WebViewClient()
                    streamView.visibility = View.VISIBLE
                    streamView.loadUrl(url)
                    streamView.settings.javaScriptEnabled = true
                }
            }
        })
    }

    private fun showSnackbar() {
        val snackbar =
            Snackbar.make(devicePageRoot, "Command Processed!", Snackbar.LENGTH_LONG)

        snackbar.view.layoutParams = (snackbar.view.layoutParams as CoordinatorLayout.LayoutParams).apply {
            anchorId = R.id.snackbarAnchor
        }
        snackbar.show()
    }

    private fun updateUi() {
        deviceName.setText(device.name())
        deviceStatus.setText(resources.getString(R.string.status, device.status()))
    }

    private fun showBottomSheet() {
        DeviceCommandBottomSheet().show(childFragmentManager, null)
    }

    companion object {
        const val DEVICE_NAME = "DEVICE_NAME"
        const val DEVICE_STATUS = "DEVICE_STATUS"
    }
}