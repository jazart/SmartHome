package com.jazart.smarthome.devicemgmt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
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
        holder.bind(devices[position], position)
    }

    override fun getItemCount(): Int = devices.size

    inner class NewDeviceHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(device: UserQuery.Device, pos: Int) {
            containerView.setOnClickListener { clickListener(device) }
            status.visibility = View.INVISIBLE
            statusColor.visibility = View.INVISIBLE
            modifyCardProperties()
            deviceName.text = device.name()
            ViewCompat.setTransitionName(deviceImage, device.name().plus(pos))
            deviceImage.deviceImage(device.type())
            statusColor.setGone()
        }

        private fun modifyCardProperties() {
            val deviceCardParams = deviceCard.layoutParams
            deviceCardParams.height = containerView.resources.displayMetrics.heightPixels.times(0.3).toInt()
            val constraintSet = ConstraintSet()
            constraintSet.clone(deviceItemConstraint)
            constraintSet.connect(deviceImage.id, ConstraintSet.START, deviceItemConstraint.id, ConstraintSet.START)
            constraintSet.connect(deviceImage.id, ConstraintSet.END, deviceItemConstraint.id, ConstraintSet.END)
            constraintSet.connect(deviceImage.id, ConstraintSet.BOTTOM, deviceItemConstraint.id, ConstraintSet.BOTTOM)
            constraintSet.connect(deviceImage.id, ConstraintSet.TOP, deviceName.id, ConstraintSet.BOTTOM)
            constraintSet.applyTo(deviceItemConstraint)
            deviceCard.layoutParams = deviceCardParams
            deviceName.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
    }
}