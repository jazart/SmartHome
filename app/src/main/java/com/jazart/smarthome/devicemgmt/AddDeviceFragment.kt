package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
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
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_top)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel = getViewModel(viewModelFactory)
        ViewCompat.setTransitionName(deviceImage, arguments?.getString("t"))
        addDeviceBtn.setOnClickListener { homeViewModel.addDevice(enterName.editText?.text.toString(), DeviceType.TV) }
    }
}