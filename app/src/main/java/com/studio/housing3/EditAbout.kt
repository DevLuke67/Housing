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
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setHeaderView(R.layout.poplocation)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("About can be updated at anytime.")
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
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Successfully updated")
                        .setTextGravity(Gravity.CENTER)
                        .setMessage("About can be updated at anytime.")
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
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/phone")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTextGravity(Gravity.CENTER)
                        .setHeaderView(R.layout.popphonel)
                        .setTitle("Successfully updated")
                        .setMessage("About can be updated at anytime.")
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
        editaboutme.setOnClickListener {
            val myid = FirebaseAuth.getInstance().uid
            val newdat = aboutmeedited.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/bio")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                            builder.setHeaderView(R.layout.popabout)
                                .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("About can be updated at anytime.")
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
            val myid = FirebaseAuth.getInstance().uid
            val newdat = editweb.text.toString()
            val getcurrent = FirebaseDatabase.getInstance().getReference("users/$myid/website")
            getcurrent.setValue(newdat)
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Successfully updated")
                        .setTextGravity(Gravity.CENTER)
                        .setHeaderView(R.layout.popweb)
                        .setMessage("About can be updated at anytime.")
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
                .addOnSuccessListener {
                    val builder = CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setHeaderView(R.layout.popgender)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Successfully updated")
                        .setMessage("About can be updated at anytime.")
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
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Successfully updated")
                        .setMessage("About can be updated at anytime.")
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
                uploadImageToFirebaseStorage()
                myprofileimageedit.setImageURI(selectedPhotoUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "$error",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun uploadImageToFirebaseStorage() {
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
                        .addOnSuccessListener {
                            val builder = CFAlertDialog.Builder(this@EditAbout)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                .setTitle("Successfully updated")
                                .setMessage("About can be updated at anytime.")
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
        ref.addValueEventListener(object : ValueEventListener {
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

                    val profileimg = profile.profileImageUrl
                    val view = myprofileimageedit
                    Glide.with(this@EditAbout).load(profileimg).placeholder(R.drawable.placeholder).into(view)

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
