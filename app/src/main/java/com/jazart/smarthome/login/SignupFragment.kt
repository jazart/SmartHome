package com.jazart.smarthome.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jazart.smarthome.R
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import com.jazart.smarthome.util.Event
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject

class SignupFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: SignupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(SignupViewModel::class.java)

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions.fitCenterTransform()
            )
            .load(R.drawable.signup)
            .into(image)
        setupTextListeners()
        signUpButton.setOnClickListener {
//            val signUpData = receivedInput()
//            when {
//                signUpData != null -> viewModel.signup(signUpData)
//                else -> Toast.makeText(requireContext(), "Both passwords must match", Toast.LENGTH_LONG).show()
//            }
        }

        viewModel.invalidLiveData.observe(viewLifecycleOwner, Observer { event: Event<String> ->
            event.consume()?.let { returnedError ->
                Toast.makeText(requireContext(), returnedError, Toast.LENGTH_LONG).show()
            }
        })
        monitorSignup()
    }

    private fun monitorSignup() {
        viewModel.signupResult.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let {
                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                Toast.makeText(requireContext(), "Welcome to SmartHome, ${userNameEt.text}", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.signUpError.observe(viewLifecycleOwner, Observer { event ->
            event.consume()?.let { msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun receivedInput(): Map<String, String>? {
        val signupInfo = mapOf(
            "firstName" to firstNameEt.text.toString(),
            "lastName" to lastNameEt.text.toString(),
            "email" to emailEt.text.toString(),
            "password" to passwordEt.text.toString(),
            "verified" to verifyPasswordEt.text.toString(),
            "username" to userNameEt.text.toString()
        )
        return if (signupInfo["password"] != signupInfo["verified"]) {
            null
        } else {
            signupInfo
        }
    }

    private fun setupTextListeners() {
        firstNameEt.validate()
        lastNameEt.validate()
        passwordEt.validate()
        verifyPasswordEt.validate()
        emailEt.validate()
        userNameEt.validate()
    }
}

fun TextInputEditText.validate() {
    val textInputLayout = this.parent.parent as TextInputLayout
    doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrBlank()) {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = "Please Complete this field"
        } else {
            textInputLayout.isErrorEnabled = false
        }
    }
}

fun TextInputEditText.isBlank(): Boolean = text.isNullOrBlank()
