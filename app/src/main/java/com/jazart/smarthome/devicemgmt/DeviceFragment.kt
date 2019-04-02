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
class DeviceFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var deviceViewModel: DeviceViewModel
    lateinit var fabViewModel: FabViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedElementEnterTransition =
//            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.explode)
        deviceViewModel = getViewModel(viewModelFactory)
        fabViewModel = getViewModel(viewModelFactory)
        observeData(deviceViewModel)
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

        deviceViewModel.currentDevice.observe(viewLifecycleOwner, Observer { updateUi(it) })
        deviceViewModel.removeDeviceResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { findNavController().navigate(R.id.homeFragment) }
        })

    }

    private fun updateUi(device: UserQuery.Device) {
        fabViewModel.iconClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                when(it) {
                    R.id.removeDevice -> {
                        findNavController().navigate(R.id.confirmDialog)
                        deviceViewModel.deleteDevice(device)
                    }
                    R.id.setFavorite -> deviceViewModel favorite device
                } }
        })
        deviceName.setText(device.name())
        deviceStatus.setText(resources.getString(R.string.status, device.status()))
    }

    private fun showBottomSheet() {
        DeviceCommandBottomSheet().show(childFragmentManager, null)
    }
}