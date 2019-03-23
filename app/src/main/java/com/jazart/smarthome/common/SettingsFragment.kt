package com.jazart.smarthome.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jazart.smarthome.R
import com.jazart.smarthome.di.Injectable

/**
 * This page displays a list of user options:
 * Logout, delete account, change password
 */

class SettingsFragment : Fragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}