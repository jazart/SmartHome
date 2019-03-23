package com.jazart.smarthome.devicemgmt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import javax.inject.Inject


class DeviceCommandBottomSheet : BottomSheetDialogFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private val commandAdapter = DeviceCommandAdapter { command -> viewModel sendCommand command; dismiss() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)
        deviceCommandRecycler.adapter = commandAdapter
        deviceCommandRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.currentDevice.observe(viewLifecycleOwner, Observer { device ->
            updateUi(device)
        })
    }

    private fun setupListeners() {

    }

    private fun updateUi(device: UserQuery.Device) {
        commandAdapter.submitList(device.commands())
    }
}
