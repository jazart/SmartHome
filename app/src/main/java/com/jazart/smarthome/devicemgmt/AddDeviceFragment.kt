package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Explode
import com.graphql.type.DeviceType
import com.jazart.smarthome.R
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_device.*
import javax.inject.Inject

class AddDeviceFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var homeViewModel: HomeViewModel
    private var deviceType = DeviceType.`$UNKNOWN`

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Explode()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(arguments != null) {
            deviceType = arguments?.getSerializable(DEVICE_TYPE) as DeviceType
        }
        deviceImage.deviceImage(deviceType)
        homeViewModel = getViewModel(viewModelFactory)
        addDeviceBtn.alpha = 0.5f
        addDeviceBtn.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
        enterName.editText?.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                addDeviceBtn.isEnabled = true
                addDeviceBtn.alpha = 1f
                addDeviceBtn.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark, null))
            } else {
                addDeviceBtn.alpha = 0.5f
                addDeviceBtn.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
                addDeviceBtn.isEnabled = false
            }
        }
        addDeviceBtn.setOnClickListener {
            homeViewModel.addDevice(
                enterName.editText?.text.toString(),
                deviceType
            )
            deviceInfo.setGone()
            addDeviceProgress.setVisible()
            hideKeyboard()
        }
        homeViewModel.addDeviceResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                Toast.makeText(requireContext(), "Device: $it has been added!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.homeFragment)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(DEVICE_TYPE, deviceType)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val DEVICE_TYPE = "device_type"
    }
}