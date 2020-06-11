package com.studio.housing3.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studio.housing3.EditAbout
import com.studio.housing3.Login
import com.studio.housing3.R
import modules.User


class FragmentProfile : Fragment() {
var root :View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         root =  inflater.inflate(R.layout.fragment_profile, container, false)
        displayprofilepic()
        val logout = root?.findViewById(R.id.logout) as ImageView
        logout.setOnClickListener {

            val builder = CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Are you Leaving?")
                .setTextGravity(Gravity.CENTER)
                .setMessage("We hope to see again soon")
            builder.setHeaderView(R.layout.poplogout)
            builder.addButton(
                "                    Leave                    ",
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#429ef4"),
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.CENTER
            ) { dialog, which ->
                //...........
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                //.........
                dialog.dismiss()
            }
            builder.show();

        }

        val edit =  root?.findViewById<ImageView>(R.id.settings)
        edit?.setOnClickListener {
        val intent = Intent(activity, EditAbout::class.java)
            startActivity(intent)
        }
        val verify = root?.findViewById(R.id.verifybtn) as TextView
        verify.setOnClickListener {
            val builder = CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Terms and Conditions Apply")
                .setTextGravity(Gravity.CENTER)
                .setMessage("By verifying your account you agree to our terms and conditions, attach legal document and other" +
                        "neccessary files. reply will be sent in 3 working days")
            builder.setHeaderView(R.layout.popverify)
            builder.addButton(
                "                    I agree                    ",
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#429ef4"),
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.CENTER
            ) { dialog, which ->
                email()
                dialog.dismiss()
            }
            builder.show();
        }

        return root
    }
    private fun email(){
        Toast.makeText(activity,"redirecting...",Toast.LENGTH_SHORT).show()
        val email = "skywayzilla.info@gmail.org"
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "$email", null
            )

        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Buying property")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Request account verification, attach legal documents"
        )
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }


        var profile: User? = null
    private fun displayprofilepic() {
        val  myprofile = FirebaseAuth.getInstance().uid
        val  ref= FirebaseDatabase.getInstance().getReference("users/$myprofile")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
               profile = p0.getValue(User::class.java)
                if (profile!=null){
                    val user = profile?.fullnames
                    val username = root?.findViewById<TextView>(R.id.myusername)
                    username?.setText(user)

                   val about = profile!!.bio
                    val aboutme = root?.findViewById<TextView>(R.id.aboutme)
                    aboutme?.setText(about)

                    val phone = profile!!.phone
                    val idphone = root?.findViewById<TextView>(R.id.phonenumber)
                    idphone?.setText(phone)

                    val email = profile!!.email
                    val idemail = root?.findViewById<TextView>(R.id.email)
                    idemail?.setText(email)

                    val location = profile!!.location
                    val idlocation = root?.findViewById<TextView>(R.id.location)
                    idlocation?.setText(location)

                    val userid = profile!!.userid
                    val iduser = root?.findViewById<TextView>(R.id.userid)
                    iduser?.setText(userid)

                    val web = profile!!.website
                    val idweb = root?.findViewById<TextView>(R.id.tvweb)
                    idweb?.setText(web)

                    val gender = profile!!.gender
                    val idgeber = root?.findViewById<TextView>(R.id.tvgender)
                    idgeber?.setText(gender)

                    val org = profile!!.organisation
                    val idorg = root?.findViewById<TextView>(R.id.tvorg)
                    idorg?.setText(org)
                    val pimage = profile?.profileImageUrl
                    val view = root?.findViewById<ImageView>(R.id.myprofileimage)
                    context?.let { Glide.with(it).load(pimage).placeholder(R.drawable.placeholder).into(view!!) }
                }else{
                    return
                }

            }
            override fun onCancelled(p0: DatabaseError) {
                return
            }

        })

    }


}

