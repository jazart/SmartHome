package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.common.ConfirmDialog
import com.jazart.smarthome.common.FabViewModel
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
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
    lateinit var fabViewModel: FabViewModel

    lateinit var device: UserQuery.Device

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
        fabViewModel = getViewModel(viewModelFactory)
        observeData(deviceViewModel)
    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putString(DEVICE_NAME, deviceName.getText())
//        outState.putString(DEVICE_STATUS, deviceStatus.getText())
//        super.onSaveInstanceState(outState)
//    }

    override fun onOptionClicked(option: Int) {
        deviceViewModel.deleteDevice(device)
    }

    private fun observeData(deviceViewModel: DeviceViewModel) {
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
        editButton.setOnClickListener {
            deviceViewModel.toggleEdit()
        }

        fabViewModel.bottomFabClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { location ->
                if (location == R.id.deviceFragment) {
                    showBottomSheet()
                }
            }
        })

        deviceViewModel.currentDevice.observe(viewLifecycleOwner, Observer {
            device = it
            updateUi(it)
        })
        deviceViewModel.removeDeviceResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { findNavController().navigate(R.id.homeFragment) }
        })

    }

    private fun updateUi(device: UserQuery.Device) {
        fabViewModel.iconClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                when (it) {
                    R.id.removeDevice -> {
                        val dialog = ConfirmDialog.newInstance("Are you sure you want to delete ${device.name()}?")
                        dialog.show(childFragmentManager, null)
                    }
                    R.id.setFavorite -> deviceViewModel favorite device
                    else -> return@let
                }
            }
        })
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