package com.jazart.smarthome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graphql.UserQuery
import com.graphql.type.Status
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_device.*

class HomeAdapter(val clickHandler: (UserQuery.Device) -> Unit) :
    ListAdapter<UserQuery.Device, HomeAdapter.HomeViewHolder>(DeviceDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_device, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class HomeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(position: Int) {
            containerView.setOnClickListener { clickHandler(getItem(position)) }
            deviceName.text = getItem(position).name()
            status.text = containerView.context.resources.getString(R.string.status, getItem(position).status())
            deviceImage.setImageResource(R.drawable.ic_lock)
            statusColor.setImageResource(
                if (getItem(position).status() == Status.CONNECTED) {
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

