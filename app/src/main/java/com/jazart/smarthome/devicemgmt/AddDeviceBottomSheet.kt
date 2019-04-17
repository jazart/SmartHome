package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.graphql.UserQuery
import com.jazart.smarthome.R
import com.jazart.smarthome.devicemgmt.AddDeviceFragment.Companion.DEVICE_TYPE
import kotlinx.android.synthetic.main.fragment_bottomsheet_add_device.*
import kotlinx.android.synthetic.main.list_item_device.*

class AddDeviceBottomSheet : BottomSheetDialogFragment() {

    private val clickHandler = { device: UserQuery.Device ->
        findNavController().navigate(
            R.id.action_to_addDeviceFragment,
            Bundle().apply {
                putString(TRANSITION, deviceImage.transitionName)
                putBoolean(FAVORITE, device.isFavorite)
                putSerializable(DEVICE_TYPE, device.type())
            },
            null,
            FragmentNavigatorExtras(deviceImage to deviceImage.transitionName)
        )
        dismiss()
    }
    private val adapter = NewDeviceAdapter(clickHandler)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet_add_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDeviceRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        addDeviceRecyclerView.adapter = adapter
        adapter.devices = HomeViewModel.testDeviceData()
    }

    interface OnDeviceClickedListener {
        fun onDeviceClicked(device: UserQuery.Device)
    }

    companion object {
        const val TRANSITION = "transition"
        const val FAVORITE = "favorite"
    }
}