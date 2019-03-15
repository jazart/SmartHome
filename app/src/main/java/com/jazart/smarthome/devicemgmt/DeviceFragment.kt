package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jazart.smarthome.R
import com.jazart.smarthome.devicemgmt.HomeFragment.Companion.ARG_DEVICE_NAME
import com.jazart.smarthome.devicemgmt.HomeFragment.Companion.ARG_DEVICE_STATUS
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

    @Inject
    lateinit var deviceViewModel: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceViewModel = ViewModelProviders.of(
            requireActivity(), viewModelFactory
        ).get(HomeViewModel::class.java)
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

        updateUi()
        editButton.setOnClickListener {
            deviceViewModel.toggleEdit()
        }

        deviceViewModel.bottomFabClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { location ->
                if (location == R.id.deviceFragment) {
                    showBottomSheet()
                }
            }
        })
    }

    private fun updateUi() {
        arguments?.run {
            deviceName.setText(this[ARG_DEVICE_NAME] as String)
            deviceStatus.setText(resources.getString(R.string.status, this[ARG_DEVICE_STATUS] as String))
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = DeviceCommandBottomSheet()
        bottomSheet.show(childFragmentManager, null)
    }
}

