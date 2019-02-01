package com.jazart.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * This fragment displays our login page. We will allow the user to sign in here. Login info will
 * be validated then sent to vm and data layer for auth with the server
 */

class LoginFragment : Fragment() {
    lateinit var viewmodel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        password_et.addTextChangedListener { text ->
            if(text.toString().isValidPass()) {
                viewmodel.login(password_et.text.toString(), text.toString())
            }
        }
    }
    override fun onStop() {
        super.onStop()
    }
}