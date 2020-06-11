package com.studio.housing3

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_about.*
import modules.User
import java.util.*

class EditAbout : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_about)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        displayabout()

        picimage.setOnClickListener {
            CropImage.activity(selectedPhotoUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

        }

        changelocation1.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = etlocation.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/location")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("Location can be updated at anytime.")
                    builder.setHeaderView(R.layout.poplocation)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }
        changeemailaddress.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = emailedit.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/email")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle("Successfully updated")
                        .setTextGravity(Gravity.CENTER)
                        .setMessage("Your clients will contact you using this new email")
                    builder.setHeaderView(R.layout.popemail)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }
        changephonenumber.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = phonenumberedit.text.toString()
            if (newdat.length <= 13){
                val builder = CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTextGravity(Gravity.CENTER)
                    .setTitle("Uknown format")
                    .setMessage("Enter your phone number in the format +254_7xxxxxxxxx")
                builder.setHeaderView(R.layout.popunknown)
                builder.addButton(
                    "CLOSE",
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#429ef4"),
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.CENTER
                ) { dialog, which ->
                    dialog.dismiss()
                }
                builder.show();
                return@setOnClickListener
            }else {

                val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/phone")
                getcurrent.setValue(newdat)
                    .addOnSuccessListener {
                        val builder = CFAlertDialog.Builder(this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                            .setTextGravity(Gravity.CENTER)
                            .setTitle("Successfully updated")
                            .setMessage("Your clients will communicate to you using your new phone number.")
                        builder.setHeaderView(R.layout.popphonel)
                        builder.addButton(
                            "CLOSE",
                            Color.parseColor("#FFFFFF"),
                            Color.parseColor("#429ef4"),
                            CFAlertDialog.CFAlertActionStyle.POSITIVE,
                            CFAlertDialog.CFAlertActionAlignment.CENTER
                        ) { dialog, which ->
                            dialog.dismiss()
                        }
                        builder.show();
                    }
            }
        }
        editaboutme.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = aboutmeedited.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/bio")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                                .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("This will us know you better")
                    builder.setHeaderView(R.layout.popabout)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }

        changeweb.setOnClickListener {
            Toast.makeText(this, "processing",Toast.LENGTH_SHORT).show()
            val myid = FirebaseAuth.getInstance().uid
            val newdat = editweb.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/website")
            getcurrent.setValue(newdat)
                .addOnCompleteListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle("Successfully updated")
                        .setTextGravity(Gravity.CENTER)
                        .setMessage("Expand your sells to more clients.")
                    builder.setHeaderView(R.layout.popweb)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }

        changegender.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = editegender.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/gender")
            getcurrent.setValue(newdat)
                .addOnCompleteListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("This will help us know you better")
                    builder.setHeaderView(R.layout.popgender)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }

        changeorganisation.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = editorg.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/organisation")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                        .setTitle("Successfully updated")
                        .setMessage("We are here to help")
                        .setTextGravity(Gravity.CENTER)
                    builder.setHeaderView(R.layout.bottom_sheet)
                    builder.addButton(
                        "CLOSE",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                selectedPhotoUri = result.uri
                myprofileimageedit.setImageURI(selectedPhotoUri)
                uploadImageToFirebaseStorage()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "$error",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun uploadImageToFirebaseStorage() {
        Toast.makeText(this,"Please wait",Toast.LENGTH_LONG).show()
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val url = it.toString()
                    val myid = FirebaseAuth.getInstance().uid
                    val path = FirebaseDatabase.getInstance().getReference("users/$myid/profileImageUrl")
                    path.setValue(url)
                        .addOnCompleteListener {
                            val builder = CFAlertDialog.Builder(this)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                                .setTitle("Successfully updated")
                                .setMessage("Your profile pic can be updated at anytime.")
                                .setTextGravity(Gravity.CENTER)
                            builder.setHeaderView(R.layout.popok)
                            builder.addButton(
                                "CLOSE",
                                Color.parseColor("#FFFFFF"),
                                Color.parseColor("#429ef4"),
                                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                                CFAlertDialog.CFAlertActionAlignment.CENTER
                            ) { dialog, which ->
                                dialog.dismiss()
                            }
                            builder.show()
                        }

                }
            }
            .addOnFailureListener {


            }
    }
    private fun displayabout() {
        val  myprofile = FirebaseAuth.getInstance().uid
        val  ref= FirebaseDatabase.getInstance().getReference("users/$myprofile")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
               val profile = p0.getValue(User::class.java)
                if (profile!=null){
                    val about = profile.bio
                    val aboutme = findViewById<TextView>(R.id.aboutmeedited)
                    aboutme?.setText(about)

                    val phone = profile.phone
                    val idphone = findViewById<TextView>(R.id.phonenumberedit)
                    idphone.setText(phone)

                    val email = profile.email
                    emailedit.setText(email)

                    val location = profile.location
                    etlocation.setText(location)

                    val web = profile.website
                    editweb.setText(web)

                    val gender = profile.gender
                    editegender.setText(gender)

                    val org = profile.organisation
                    editorg.setText(org)

                   val RequestManager : RequestManager = Glide.with(this@EditAbout)

                    val profileimg = profile.profileImageUrl
                    val view = myprofileimageedit
                    RequestManager.load(profileimg).placeholder(R.drawable.placeholder).into(view)

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
