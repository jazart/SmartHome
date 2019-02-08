package com.jazart.smarthome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jazart.smarthome.models.Device
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_device.*

class HomeAdapter : ListAdapter<Device, HomeAdapter.HomeViewHolder>(DeviceDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_device, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class HomeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(position: Int) {
            deviceName.text = getItem(position).name
            status.text = containerView.context.resources.getString(R.string.status, "Connected")
            deviceImage.setImageResource(R.drawable.ic_lock)
        }
    }

}

class DeviceDiff : DiffUtil.ItemCallback<Device>() {
    override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean = oldItem == newItem
}

