package com.jazart.smarthome.devicemgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graphql.type.Command
import com.jazart.smarthome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_command.*

class DeviceCommandAdapter(clickListener: (Command) -> Unit) :
    ListAdapter<Command, DeviceCommandAdapter.DeviceViewHolder>(object : DiffUtil.ItemCallback<Command>() {
        override fun areItemsTheSame(oldItem: Command, newItem: Command): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Command, newItem: Command): Boolean = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_command,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeviceViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(command: Command) {
            commandText.text = command.toString()
        }
    }

}