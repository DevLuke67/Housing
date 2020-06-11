package com.studio.housing3

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
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
import kotlinx.android.synthetic.main.activity_rent.*
import kotlinx.android.synthetic.main.activity_view_sales.*
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
             progress.visibility = View.VISIBLE
             val input1 = lhomeocation.text.toString()
             val input2 = homename.text.toString()
             val p1 = pricerange1.text.toString()
             val p2 = pricerange2.text.toString()
             val about = abouthome.text.toString()
             val img1 = selectedPhotoUri
             val img2 = selectedPhoto2Uri
             val img3 = selectedPhoto3Uri
             if (input1.isEmpty() || input2.isEmpty() || p1.isEmpty() || p2.isEmpty() || about.isEmpty() || img1 == null || img2 == null || img3 == null){
                 progress.visibility = View.INVISIBLE
                 val builder = CFAlertDialog.Builder(this)
                     .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                     .setTitle("Not so fast!")
                     .setTextGravity(Gravity.CENTER)
                     .setMessage("Some details are missing, please check your details.")
                 builder.setHeaderView(R.layout.popstop)
                 builder.addButton(
                     "                    Alright                    ",
                     Color.parseColor("#FFFFFF"),
                     Color.parseColor("#429ef4"),
                     CFAlertDialog.CFAlertActionStyle.POSITIVE,
                     CFAlertDialog.CFAlertActionAlignment.CENTER

                 ) { dialog, which ->
                     //...........
                     //.........
                     dialog.dismiss()
                 }
                 builder.show();
             }else{
                 checkinfo()
             }

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
                    val phone = info.phone
                    val timestamp = formatedDate.toString()
                    val path = FirebaseDatabase.getInstance().reference.child("posts/$randomuid")
                    val sales = Sales(myid, fullnames,profileimageurl,email, postcode,homename,location,propertytype!!,rooms!!,bedrooms!!,bathrooms!!,furnishing!!,price1,price2,abouthome,image1 = "",image2 = "",image3 = "",timestamp = timestamp,phone = phone)
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
    private fun checkinfo() {
        val  myprofile = FirebaseAuth.getInstance().uid
        val  ref= FirebaseDatabase.getInstance().getReference("users/$myprofile")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val profile = p0.getValue(User::class.java)
              if (profile?.profileImageUrl == null || profile.phone == "Not updated" || profile.gender == "Not specified" || profile.website == "Not available" || profile.location == "Not updated" || profile.bio == "Not updated"|| profile.email == ""){
                  progress.visibility = View.INVISIBLE
                  val builder = CFAlertDialog.Builder(this@Rent)
                      .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                      .setTitle("Oops! You Don't Qualify")
                      .setTextGravity(Gravity.CENTER)
                      .setMessage("Complete your profile and try again")
                  builder.setHeaderView(R.layout.popqualify)
                  builder.addButton(
                      "                    OK                    ",
                      Color.parseColor("#FFFFFF"),
                      Color.parseColor("#429ef4"),
                      CFAlertDialog.CFAlertActionStyle.POSITIVE,
                      CFAlertDialog.CFAlertActionAlignment.CENTER
                  ) { dialog, which ->
                      //...........
                      //.........
                      dialog.dismiss()
                  }
                  builder.show();
              }else{
                  savePostToFirebaseDatabase()
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
        val ref = System.currentTimeMillis()/10
        val ref2 = System.currentTimeMillis()*3
        val ref3 = System.currentTimeMillis()*7

        val path = FirebaseStorage.getInstance().getReference("posts/$myid/$ref")
        val path2 = FirebaseStorage.getInstance().getReference("posts/$myid/$ref2")
        val path3 = FirebaseStorage.getInstance().getReference("posts/$myid/$ref3/")

        path.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                path.downloadUrl.addOnSuccessListener {
                    val url1 = it.toString()
                    val db = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image1")
                    db.setValue(url1)
                }
            }
        path2.putFile(selectedPhoto2Uri!!)
            .addOnSuccessListener {
                path2.downloadUrl.addOnSuccessListener {
                    val url2 = it.toString()
                    val db = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image2")
                    db.setValue(url2)
                }
            }
        path3.putFile(selectedPhoto3Uri!!)
            .addOnSuccessListener {
                path3.downloadUrl.addOnSuccessListener {
                    val url3 = it.toString()
                    val db = FirebaseDatabase.getInstance().getReference("posts/$randomuid/image3")
                    db.setValue(url3)
                    Toast.makeText(this, "Completed",Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }


    }
    var selectedroom :String? = null
    var selectedproperty: String? = null
    var selectedbathroom:String? = null
    var selectedbedroom:String? = null
    var selectedfurnishing:String? = null
}




