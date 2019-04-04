package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = Slide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as ViewGroup).doOnPreDraw { startPostponedEnterTransition() }
        homeViewModel = getViewModel(viewModelFactory)
        ViewCompat.setTransitionName(deviceImage, arguments?.getString("t"))
        addDeviceBtn.setOnClickListener {
            homeViewModel.addDevice(
                enterName.editText?.text.toString(),
                DeviceType.CAMERA,
                arguments?.getBoolean("fav") ?: false
            )
            deviceInfo.setGone()
            addDeviceProgress.show()
        }
        homeViewModel.addDeviceResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                Toast.makeText(requireContext(), "Device: $it has been added!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.homeFragment)
            }
        })
    }
}