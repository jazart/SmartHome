package com.jazart.smarthome.devicemgmt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.graphql.UserQuery
import com.graphql.type.Command
import com.jazart.smarthome.R
import kotlinx.android.synthetic.main.fragment_bottomsheet_add_device.*

class AddDeviceBottomSheet : BottomSheetDialogFragment() {

    private val clickHandler = { device: UserQuery.Device ->
        (parentFragment as OnDeviceClickedListener).onDeviceClicked(device)
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
        adapter.devices = listOf(
            UserQuery.Device(
                "TV",
                "Television",
                com.graphql.type.Status.CONNECTED,
                listOf(Command.TURN_ON, Command.TURN_ON, Command.TURN_OFF, Command.PULSE),
                "jeremy"
            ),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy"),
            UserQuery.Device("Camera", "Test Device", com.graphql.type.Status.CONNECTED, listOf(), "jeremy")
        )
    }

    interface OnDeviceClickedListener {
        fun onDeviceClicked(device: UserQuery.Device)
    }
}