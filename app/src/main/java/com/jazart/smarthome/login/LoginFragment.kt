package com.jazart.smarthome.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.jazart.smarthome.R
import com.jazart.smarthome.common.MainActivity
import com.jazart.smarthome.di.Injectable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

/**
 * This fragment displays our login page. We will allow the user to sign in here. Login info will
 * be validated then sent to vm and data layer for auth with the server
 */

class LoginFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).apply {
            bottomFab.visibility = View.GONE
            bottom_bar.visibility = View.GONE
        }
        val viewModel = ViewModelProviders.of(requireActivity(), viewModelProvider).get(LoginViewModel::class.java)

        login_btn.setOnClickListener {
            viewModel.login(username_et.text.toString(), password_et.text.toString())
        }
        viewModel.loginEvent.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { token ->
                if (token.isNotBlank()) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    Toast.makeText(requireContext(), getString(R.string.welcome, "${username_et.text}"), Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.loginError.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { err ->
                Toast.makeText(requireContext(), err.name, Toast.LENGTH_LONG).show()
            }
        })

        register_btn.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signupFragment) }

    }

    override fun onStop() {
        super.onStop()
    }
}