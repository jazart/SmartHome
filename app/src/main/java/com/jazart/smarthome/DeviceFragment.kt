package com.jazart.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jazart.smarthome.HomeFragment.Companion.ARG_DEVICE_NAME
import com.jazart.smarthome.HomeFragment.Companion.ARG_DEVICE_STATUS
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceViewModel = ViewModelProviders.of(this, viewModelFactory).get(DeviceViewModel::class.java)
        if (arguments != null) {
            updateUi()
        }
    }

    private fun updateUi() {
        deviceName.text = arguments!![ARG_DEVICE_NAME] as String
        deviceStatus.text = arguments!![ARG_DEVICE_STATUS] as String
    }
}
