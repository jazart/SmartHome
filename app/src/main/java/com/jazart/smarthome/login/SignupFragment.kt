package com.jazart.smarthome.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jazart.smarthome.R
import com.jazart.smarthome.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject

class SignupFragment : Fragment() {

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
            receivedInput()?.let { signupInfo ->
                viewModel.signup(signupInfo)
            }
            Toast.makeText(requireContext(), "Please complete all of the fields.", Toast.LENGTH_LONG).show()
        }
    }


    private fun receivedInput(): List<String>? {
        val receivedFirstName = firstNameEt.text.toString()
        val receivedLastName = lastNameEt.text.toString()
        val receivedEmail = emailEt.text.toString()
        val receivedPassword = passwordEt.text.toString()
        val receivedVerified = verifyPasswordEt.text.toString()
        val receivedUsername = userNameEt.text.toString()

        if (receivedPassword != receivedVerified) {
            return null
        }
        if (receivedFirstName.isBlank() || receivedLastName.isBlank() ||
            receivedEmail.isBlank() || receivedPassword.isBlank() ||
            receivedVerified.isBlank()
        ) {
            return null
        }
        return listOf(receivedFirstName, receivedLastName, receivedEmail, receivedUsername, receivedPassword)
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
    doOnTextChanged { text, start, count, after ->
        if (text.isNullOrBlank()) {

            textInputLayout.isErrorEnabled = false
            textInputLayout.error = "Please Complete this field"
        } else {
            textInputLayout.isErrorEnabled = false

        }
    }

}

fun TextInputEditText.isBlank(): Boolean = text.isNullOrBlank()
