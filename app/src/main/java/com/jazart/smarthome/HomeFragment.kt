package com.jazart.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graphql.UserQuery
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_device.*
import javax.inject.Inject

/**
 * This page will display the user's favorite device and its status
 * and also a list of their other devices. There will be a bottom menu bar
 * as well as an option to add another favorite or add/remove a device
 */

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: HomeViewModel

    private val clickHandler: (Int, UserQuery.Device) -> Unit = { pos, device ->
        viewModel.updateCurrentDevice(pos)
        findNavController().navigate(R.id.deviceFragment, buildBundle(device))
    }

    private val adapter = HomeAdapter(clickHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)
        viewModel.loadDevices()
        viewModel.devices.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        deviceImage.setImageResource(R.drawable.ic_lock)
        editableTV.text = "Favorite Device"
        status.text = getString(R.string.status, "Offline")
        home_recyclerView.adapter = adapter
        home_recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        const val ARG_DEVICE_NAME = "ARG_DEVICE_NAME"
        const val ARG_DEVICE_STATUS = "ARG_DEVICE_STATUS"
        const val ARG_DEVICE_COMMANDS = "ARG_DEVICE_COMMANDS"

        fun buildBundle(device: UserQuery.Device): Bundle = Bundle().apply {
            putString(ARG_DEVICE_NAME, device.name())
            putString(ARG_DEVICE_STATUS, "${device.status()}")
            putStringArrayList(ARG_DEVICE_COMMANDS, device.info().commands().toStringList().toCollection(ArrayList()))
        }
    }
}

private fun <E> List<E>.toStringList(): List<String> {
    return this.map { obj -> "$obj" }
}
