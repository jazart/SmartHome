package com.jazart.smarthome.devicemgmt

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.common.FabViewModel
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

class HomeFragment : Fragment(), Injectable, AddDeviceBottomSheet.OnDeviceClickedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var fabViewModel: FabViewModel

    private val clickHandler: (Int, UserQuery.Device) -> Unit = { pos, device ->
        deviceViewModel.initCurrentDevice(device)
        findNavController().navigate(
            R.id.action_homeFragment_to_deviceFragment,
            buildBundle(device)
        )
    }

    private val adapter = HomeAdapter(clickHandler)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            requireActivity().getSharedPreferences("user_jwt", Context.MODE_PRIVATE).edit().clear().apply()
            requireActivity().finishAndRemoveTask()
        }
        homeViewModel = getViewModel(viewModelFactory)
        deviceViewModel = getViewModel(viewModelFactory)
        fabViewModel = getViewModel(viewModelFactory)
        homeViewModel.loadDevices()
        observeLiveData()
        deviceImage.setImageResource(R.drawable.ic_lock)
        deviceName.text = getString(R.string.fav_device)
        status.text = getString(R.string.status, "Offline")
        home_recyclerView.adapter = adapter
        home_recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

    }

    override fun onDeviceClicked(device: UserQuery.Device) {
        view?.rootView?.let { Snackbar.make(it, device.name(), Snackbar.LENGTH_LONG).show() }
    }

    private fun observeLiveData() {
        homeViewModel.devices.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
//                displayAddDeviceUi()
            } else {
                adapter.submitList(it)
            }
        })
        homeViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            val span = SpannableString(getString(R.string.home_greeting, user))
            span.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorAccent, null)),
                5,
                span.lastIndexOf(user) + user.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            userGreeting.text = span
        })
        fabViewModel.bottomFabClicked.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { id ->
                if (id == findNavController().currentDestination?.id) {
                    showBottomSheet()
                }
            }
        })
    }

    private fun displayAddDeviceUi() {
        home_recyclerView.setGone()
        favDevice.setGone()
        userGreeting.setGone()
        homeFragmentRoot.background = resources.getDrawable(R.drawable.background, null)
    }

    private fun showBottomSheet() {
        AddDeviceBottomSheet().show(childFragmentManager, null)
    }

    companion object {
        const val TAG = "HomeFragment"
        const val ARG_DEVICE_NAME = "ARG_DEVICE_NAME"
        const val ARG_DEVICE_STATUS = "ARG_DEVICE_STATUS"
        const val ARG_DEVICE_OWNER = "ARG_DEVICE_OWNER"
        private const val ARG_DEVICE_COMMANDS = "ARG_DEVICE_COMMANDS"

        fun buildBundle(device: UserQuery.Device): Bundle = Bundle().apply {
            putString(ARG_DEVICE_NAME, device.name())
            putString(ARG_DEVICE_OWNER, device.owner())
            putString(ARG_DEVICE_STATUS, "${device.status()}")
            putStringArrayList(ARG_DEVICE_COMMANDS, device.commands()?.toStringList()?.toCollection(ArrayList()))
        }
    }
}

private fun <E> List<E>.toStringList(): List<String> {
    return this.map { obj -> "$obj" }
}

fun View.setGone() {
    visibility = View.GONE
}

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(factory: ViewModelFactory): T =
    ViewModelProviders.of(this, factory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getViewModel(factory: ViewModelFactory): T {
    return ViewModelProviders.of(requireActivity(), factory)[T::class.java]
}