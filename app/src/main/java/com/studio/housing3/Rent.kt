package com.studio.housing3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_rent.*
import modules.Sales
import modules.User
import java.text.SimpleDateFormat
import java.util.*

class Rent : AppCompatActivity(){

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_rent)
         val actionBar: ActionBar? = supportActionBar
         actionBar?.hide()

         submit.setOnClickListener {
             savePostToFirebaseDatabase()
         }
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
         val property: Spinner = findViewById(R.id.propertytype)
// Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter.createFromResource(
             this,
             R.array.list_of_property,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             // Apply the adapter to the spinner
             property.adapter = adapter
             property.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                 override fun onNothingSelected(parent: AdapterView<*>?) {
                     selectedproperty = parent?.selectedItem as String
                     Log.d("tt","property : $selectedproperty")
                 }

                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     selectedproperty = parent.selectedItem as String
                     Log.d("tt","property : $selectedproperty")

                 }
             }

         }

         val bathroom: Spinner = findViewById(R.id.bathrooms)
// Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter.createFromResource(
             this,
             R.array.list_of_bathrooms,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             // Apply the adapter to the spinner
             bathroom.adapter = adapter
             bathroom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                 override fun onNothingSelected(parent: AdapterView<*>?) {
                     selectedbathroom = parent?.selectedItem as String
                     Log.d("tt","bathroom : $selectedbathroom")
                 }

                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     selectedbathroom = parent.selectedItem as String
                     Log.d("tt","bathrooms : $selectedbathroom")

                 }
             }

         }

         val bedroom: Spinner = findViewById(R.id.bedrooms)
// Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter.createFromResource(
             this,
             R.array.list_of_bedrooms,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             // Apply the adapter to the spinner
             bedroom.adapter = adapter
             bedroom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                 override fun onNothingSelected(parent: AdapterView<*>?) {
                     selectedbedroom = parent?.selectedItem as String
                     Log.d("tt","bedroom : $selectedbedroom")
                 }

                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     selectedbedroom = parent.selectedItem as String
                     Log.d("tt","bedroom : $selectedbedroom")

                 }
             }

         }

         val furnished: Spinner = findViewById(R.id.funishing)
// Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter.createFromResource(
             this,
             R.array.either_furnished,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             // Apply the adapter to the spinner
             furnished.adapter = adapter
             furnished.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                 override fun onNothingSelected(parent: AdapterView<*>?) {
                     selectedfurnishing = parent?.selectedItem as String
                     Log.d("tt","furnish : $selectedfurnishing")
                 }

                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     selectedfurnishing = parent.selectedItem as String
                     Log.d("tt","furnish : $selectedfurnishing")

                 }
             }

         }
         val rooms: Spinner = findViewById(R.id.numberofrooms)

// Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter.createFromResource(
             this,
             R.array.list_of_rooms,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             // Apply the adapter to the spinner
             rooms.adapter = adapter


             rooms.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                 override fun onNothingSelected(parent: AdapterView<*>?) {

                 }

                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     selectedroom = parent.selectedItem as String
                     Log.d("tt","room : $selectedroom")

                 }
             }
         }
         pic2.setOnClickListener {
             val intent = Intent(Intent.ACTION_PICK)
             intent.type = "image/*"
             startActivityForResult(intent, 0)

         }
         picone.setOnClickListener {
             val intent = Intent(Intent.ACTION_PICK)
             intent.type = "image/*"
             startActivityForResult(intent, 1)
         }
         pic3.setOnClickListener {
             val intent = Intent(Intent.ACTION_PICK)
             intent.type = "image/*"
             startActivityForResult(intent, 2)
         }
         }
    var selectedPhotoUri: Uri? = null
    var selectedPhoto2Uri: Uri? = null
    var selectedPhoto3Uri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0){
            selectedPhotoUri = data?.data
            pic2.setImageURI(selectedPhotoUri)
            Toast.makeText(this,"first photo is ready",Toast.LENGTH_LONG).show()
        }else if (resultCode == Activity.RESULT_OK && requestCode == 1){
            selectedPhoto2Uri = data?.data
            picone.setImageURI(selectedPhoto2Uri)
            Toast.makeText(this,"second photo is ready",Toast.LENGTH_LONG).show()
        }else if (resultCode == Activity.RESULT_OK && requestCode == 2){
            selectedPhoto3Uri = data?.data
            pic3.setImageURI(selectedPhoto3Uri)
            Toast.makeText(this,"last photo is ready",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()
            return
        }
    }
    val randomuid = UUID.randomUUID().mostSignificantBits.toString()
    private fun savePostToFirebaseDatabase() {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        progress.visibility = View.VISIBLE
        var filename = System.currentTimeMillis()/1000
        val postcode = filename.toString()
        val location = lhomeocation.text.toString()
        val myid = FirebaseAuth.getInstance().uid.toString()

        val propertytype = selectedproperty
        val rooms = selectedroom
        val bedrooms = selectedbedroom
        val bathrooms = selectedbathroom
        val furnishing = selectedfurnishing
        val image2 = selectedPhotoUri
        val image1 = selectedPhoto2Uri
        val image3 = selectedPhoto3Uri
        val price1 = pricerange1.text.toString()
        val price2 = pricerange2.text.toString()
        val abouthome = abouthome.text.toString()
        val user = FirebaseAuth.getInstance().uid
        val homename = homename.text.toString()
        val userinfo = FirebaseDatabase.getInstance().reference.child("/users/$user")

        userinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val info = p0.getValue(User::class.java)
                if (info != null){
                    val fullnames = info.fullnames
                    val profileimageurl = info.profileImageUrl
                    val email = info.email
                    val timestamp = formatedDate.toString()
                    val path = FirebaseDatabase.getInstance().reference.child("posts/$randomuid")
                    val sales = Sales(myid, fullnames,profileimageurl,email, postcode,homename,location,propertytype!!,rooms!!,bedrooms!!,bathrooms!!,furnishing!!,price1,price2,abouthome,image1 = "",image2 = "",image3 = "",timestamp = timestamp)
                    path.setValue(sales).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                        uploadImageToFirebaseStorage()
                        } else {
return@addOnCompleteListener
                        }

                    }

                }

            }
            override fun onCancelled(p0: DatabaseError) {
                return
            }
        })

    }

    private fun uploadImageToFirebaseStorage() {
        progress.visibility = View.VISIBLE
        if (selectedPhotoUri == null || selectedPhoto2Uri == null || selectedPhoto3Uri == null) return
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        val myid = FirebaseAuth.getInstance().uid.toString()

        val ref = FirebaseStorage.getInstance().getReference("/posts/$randomuid")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val image2url = it.toString()
                    val set = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image2")
                    set.setValue(image2url)
                    ref.putFile(selectedPhoto2Uri!!)
                        .addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                val image1url = it.toString()
                                val set2 = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image1")
                                set2.setValue(image1url)
                                ref.putFile(selectedPhoto3Uri!!)
                                    .addOnSuccessListener {
                                        ref.downloadUrl.addOnSuccessListener {
                                            val image3url = it.toString()
                                            val set3 = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image3")
                                            set3.setValue(image3url)
                                            progress.visibility = View.INVISIBLE
                                            Toast.makeText(this, "Complete",Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        }

                }
            }
            .addOnFailureListener {

            }
    }
    var selectedroom :String? = null
    var selectedproperty: String? = null
    var selectedbathroom:String? = null
    var selectedbedroom:String? = null
    var selectedfurnishing:String? = null
}




