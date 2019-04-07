package com.jazart.smarthome.devicemgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graphql.UserQuery
import com.graphql.type.DeviceType
import com.graphql.type.Status
import com.jazart.smarthome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_device.*

class HomeAdapter(val clickHandler: (Int, UserQuery.Device) -> Unit) :
    ListAdapter<UserQuery.Device, HomeAdapter.HomeViewHolder>(DeviceDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_device, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class HomeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        internal fun bind(position: Int) {
            val device = getItem(position)
            containerView.setOnClickListener { clickHandler(position, device) }
            deviceName.text = device.name()
            status.text = containerView.context.resources.getString(R.string.status, device.status())
            deviceImage.deviceImage(device.type())
            statusColor.setImageResource(
                if (device.status() == Status.CONNECTED) {
                    android.R.color.holo_green_light
                } else {
                    android.R.color.holo_red_light
                }
            )
        }
    }

}

class DeviceDiff : DiffUtil.ItemCallback<UserQuery.Device>() {
    override fun areItemsTheSame(oldItem: UserQuery.Device, newItem: UserQuery.Device): Boolean =
        oldItem.name() == newItem.name()

    override fun areContentsTheSame(oldItem: UserQuery.Device, newItem: UserQuery.Device): Boolean = oldItem == newItem
}

fun AppCompatImageView.deviceImage(type: DeviceType) {
    setImageResource(
        when (type) {
            DeviceType.CAMERA -> R.drawable.ic_video_camera
            DeviceType.LIGHT -> R.drawable.ic_lightbulb_outline_black_24dp
            DeviceType.TV -> R.drawable.ic_tv
            DeviceType.HOME_TEMPERATURE -> R.drawable.ic_temperature
            DeviceType.MOTION -> R.drawable.ic_motion_sensor
            DeviceType.BLUETOOTH_DEVICE -> R.drawable.ic_bluetooth
            DeviceType.`$UNKNOWN` -> R.drawable.ic_info
        }
    )
}