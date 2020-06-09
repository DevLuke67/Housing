package com.studio.housing3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyifuserisloggedin()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_explore, R.id.navigation_wish, R.id.navigation_notification, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val rent = findViewById<View>(R.id.navigation_rent)
        rent.setOnClickListener {
            intent = Intent(this, Rent::class.java)
            startActivity(intent)
        }

    }

    private fun verifyifuserisloggedin() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginRegister::class.java)
            startActivity(intent)
            finish()
        }
    }
    companion object {
        const val ARG_NAME = "name"

    }
}


