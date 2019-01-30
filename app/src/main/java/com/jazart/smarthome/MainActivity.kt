package com.jazart.smarthome

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_btn.setOnClickListener {
            var status = if (username_et.text.toString().equals("SmartHome")
                && password_et.text.toString().equals("home")
            ) "Logged In Successfully" else "Login failed"

            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }
}
