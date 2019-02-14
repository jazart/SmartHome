package com.jazart.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.models.Device
import com.jazart.smarthome.models.Status
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_device.*

/**
 * This page will display the user's favorite device and its status
 * and also a list of their other devices. There will be a bottom menu bar
 * as well as an option to add another favorite or add/remove a device
 */


class HomeFragment : Fragment(), Injectable {
    private val adapter = HomeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(
            listOf(
                Device("Test", Status.Off),
                Device("Test1", Status.Off),
                Device("Test2", Status.Off),
                Device("Test3", Status.Off),
                Device("Test4", Status.Off),
                Device("Test5", Status.Off),
                Device("Test6", Status.Off),
                Device("Test3", Status.Off),
                Device("Test4", Status.Off),
                Device("Test5", Status.Off),
                Device("Test6", Status.Off)
            )
        )
        deviceImage.setImageResource(R.drawable.ic_lock)
        deviceName.text = "Favorite Device"
        status.text = getString(R.string.status, "Offline")
        home_recyclerView.adapter = adapter
        home_recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

    }

    override fun onStop() {
        super.onStop()
    }
}