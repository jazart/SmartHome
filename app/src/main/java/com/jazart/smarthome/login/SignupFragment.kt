package com.jazart.smarthome.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jazart.smarthome.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions.fitCenterTransform()
            )
            .load(R.drawable.signup)
            .into(image)
        setupTextListeners()
        signUpButton.setOnClickListener {
            receivedInput()
        }
    }


    fun receivedInput(): Boolean? {
        val receivedFirstName = firstNameEt.text.toString()
        val receivedLastName = lastNameEt.text.toString()
        val receivedEmail = emailEt.text.toString()
        val receivedPassword = passwordEt.text.toString()
        val receivedVerified = verifyPasswordEt.text.toString()

        if (receivedPassword != receivedVerified) {
            return null
        }
        return false
    }

    private fun setupTextListeners() {
        firstNameEt.validate()
        lastNameEt.validate()
        passwordEt.validate()
        verifyPasswordEt.validate()
        emailEt.validate()
        username_et.validate()
    }
}

fun TextInputEditText.validate() {
    val textInputLayout = this.parent as TextInputLayout
    doOnTextChanged { text, start, count, after ->
        if (text.isNullOrBlank()) {

            textInputLayout.isErrorEnabled = false
            textInputLayout.error = "Please Complete this field"
        } else {
            textInputLayout.isErrorEnabled = false

        }
    }
}
