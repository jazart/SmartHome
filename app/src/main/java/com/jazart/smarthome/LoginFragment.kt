package com.jazart.smarthome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jazart.smarthome.di.App
import com.jazart.smarthome.di.Injectable
import javax.inject.Inject

/**
 * This fragment displays our login page. We will allow the user to sign in here. Login info will
 * be validated then sent to vm and data layer for auth with the server
 */

class LoginFragment : Fragment(), Injectable {
    @Inject lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = ViewModelProviders.of(requireActivity(), viewModelProvider).get(LoginViewModel::class.java)

    }
    override fun onStop() {
        super.onStop()
    }
}