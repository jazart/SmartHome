package com.jazart.smarthome.devicemgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.graphql.type.Command

class DeviceCommandAdapter(clickListener: (Command) -> Unit) :
    RecyclerView.Adapter<DeviceCommandAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder =
        DeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = 0

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {

        }
    }

}