package com.studio.housing3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegister : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        tologin.setOnClickListener {
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        toregister.setOnClickListener {
            intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
