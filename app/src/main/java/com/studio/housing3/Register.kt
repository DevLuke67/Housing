package com.studio.housing3

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import modules.User

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        backonpressed.setOnClickListener {
            onBackPressed()

        }
        loginredirect.setOnClickListener{
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
createaccount.setOnClickListener {
    performRegister()
}
    }
    private fun performRegister() {
        val email = emailaddress.text.toString()
        val password = password.text.toString()
        val confirmpass = confirmpassword.text.toString()

        var progress = ProgressDialog(this)
        progress.setMessage("Creating account...")


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT).show()

        }else if (password != confirmpass){
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_LONG).show()
        }else {

            progress.show()
            // Firebase Authentication to create a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    saveUserToFirebaseDatabase()
                }
                .addOnFailureListener {
                    progress.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("The email address is already in use by another account. Already have an account? Sign In")
                    builder.setPositiveButton("Ok") { dialog, which ->
                        // Do something when user press the positive button
                        dialog.dismiss()
                    }

                    // Display a negative button on alert dialog
                    builder.setNegativeButton("Sign In") { dialog, which ->
                        intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()



                }
        }
    }

    private fun saveUserToFirebaseDatabase() {
        val fullnames = fullnames.text.toString()
        val email = emailaddress.text.toString()
        val profileImageUrl = "https://firebasestorage.googleapis.com/v0/b/housing-62cbc.appspot.com/o/profile%2Fcreateprofile.png?alt=media&token=93db308b-2a8b-4f8d-940d-17a56bfe4644"
        val bio = "Not updated"
        val location = "Not updated"
        val phone = "Not updated"
        val userid = System.currentTimeMillis()/10000
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, fullnames, email, profileImageUrl, bio, phone, location, userid.toString(), website = "Not available",gender = "Not specified",organisation = "Independent")

        var progress = ProgressDialog(this)
        progress.setMessage("Loging in...")
        progress.show()
        ref.setValue(user)
            .addOnSuccessListener {
                progress.dismiss()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login Failed",Toast.LENGTH_LONG).show()
                progress.dismiss()
                return@addOnFailureListener
            }
    }
}
