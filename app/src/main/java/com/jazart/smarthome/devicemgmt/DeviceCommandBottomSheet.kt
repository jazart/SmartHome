package com.jazart.smarthome.devicemgmt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.R
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import javax.inject.Inject


class DeviceCommandBottomSheet : BottomSheetDialogFragment(), Injectable, View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: HomeViewModel
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
        viewModel.currentDevice.observe(viewLifecycleOwner, Observer { device ->
            updateUi(device)
        })
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.lightOffBtn -> viewModel sendCommand Command.TURN_OFF
            R.id.lightOnBtn -> viewModel sendCommand Command.TURN_ON
            R.id.lightPulseBtn -> viewModel sendCommand Command.PULSE
            else -> Toast.makeText(requireContext(), getString(R.string.invalid_command), Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }

    private fun setupListeners() {
        lightOffBtn.setOnClickListener(this)
        lightOnBtn.setOnClickListener(this)
        lightPulseBtn.setOnClickListener(this)
    }

    private fun updateUi(device: UserQuery.Device) {

    }
}
