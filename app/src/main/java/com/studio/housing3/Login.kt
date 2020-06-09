package com.studio.housing3

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        backonpressedtogin.setOnClickListener {
            onBackPressed()
        }

        loginacc.setOnClickListener {
            var email = emailaddresslogin.text.toString()
            var password = passwordlogin.text.toString()

            if (email.isEmpty() or password.isEmpty()) {
                Toast.makeText(this, "Fill in the required fields", Toast.LENGTH_SHORT).show()
            } else {

                var progress = ProgressDialog(this)
                progress.setMessage("Authenticating...")
                progress.show()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        progress.dismiss()
                        Toast.makeText(this, "successfully logged in", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        progress.dismiss()

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Login Failed")
                        builder.setMessage("Wrong email or password, try again")
                        builder.setPositiveButton("Retry") { dialog, which ->
                            // Do something when user press the positive button
                            dialog.dismiss()
                        }

                        // Display a negative button on alert dialog
                        builder.setNegativeButton("sign up") { dialog, which ->
                            intent = Intent(this, Register::class.java)
                            startActivity(intent)
                        }
                        // Finally, make the alert dialog using builder
                        val dialog: AlertDialog = builder.create()

                        // Display the alert dialog on app interface
                        dialog.show()


                    }
            }

        }
    }
}
