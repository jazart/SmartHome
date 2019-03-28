package com.jazart.smarthome.devicemgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.graphql.UserQuery
import com.jazart.smarthome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_device.*

class NewDeviceAdapter(
    private val clickListener: (UserQuery.Device) -> Unit,
    var devices: List<UserQuery.Device> = listOf()
) :
    RecyclerView.Adapter<NewDeviceAdapter.NewDeviceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewDeviceHolder {
        return NewDeviceHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_device, parent, false))
    }

    override fun onBindViewHolder(holder: NewDeviceHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size

    inner class NewDeviceHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(device: UserQuery.Device) {
            containerView.setOnClickListener { clickListener(device) }
            status.setGone()
            val deviceCardParams = deviceCard.layoutParams
            deviceCardParams.height = containerView.resources.displayMetrics.heightPixels.times(0.3).toInt()
            deviceCard.layoutParams = deviceCardParams
            deviceName.text = device.name()
            statusColor.setGone()
        }
    }
}