package com.studio.housing3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.collection.arraySetOf
import com.bumptech.glide.Glide
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studio.housing3.ui.dashboard.ExploreFragment.Companion.USER
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_edit_about.*
import kotlinx.android.synthetic.main.activity_view_sales.*
import kotlinx.android.synthetic.main.fragment_wish.*
import modules.Request
import modules.Sales
import modules.User
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

class ViewSales : AppCompatActivity() {
    var postpreview: Sales? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sales)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        postpreview = intent.getParcelableExtra<Sales>(USER)
        displayproperty()
        var sampleImages = arrayOf<String>(
        postpreview?.image1.toString(),
        postpreview?.image2.toString(),
            postpreview?.image3.toString()
        )
        carouselView.setPageCount(sampleImages.size);
        carouselView.setImageListener(object : ImageListener{
            override fun setImageForPosition(position: Int, imageView: ImageView?) {
                if (imageView != null) {
                    Glide.with(this@ViewSales).load(sampleImages[position]).into(imageView)
                }
            }
        });

        call.setOnClickListener {
            Toast.makeText(this, "calling",Toast.LENGTH_SHORT).show()
            makePhoneCall(it.toString())
        }
        fun openNewTabWindow(urls: String, context: Context) {
            val uris = Uri.parse(urls)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window", true)
            intents.putExtras(b)
            context.startActivity(intents)
        }

    whatsapp.setOnClickListener {
        Toast.makeText(this,"redirecting...",Toast.LENGTH_SHORT).show()
        val phone = postpreview?.phone
        openNewTabWindow("https://api.whatsapp.com/send?phone=$phone&text=I'm+interested+in+buying+your+home",this)
    }
sendemail.setOnClickListener {
email()
}

        wish.setOnClickListener {
        confirmsave()
        }

        rentnow.setOnClickListener {
            Toast.makeText(this,"Processing",Toast.LENGTH_LONG).show()
            sendrequest()
        }
    }
private fun confirmsave(){
    val myid = FirebaseAuth.getInstance().uid
    val postcode = postpreview?.postcode
    val ref = FirebaseDatabase.getInstance().getReference("saves/$myid/$postcode")
    ref.addListenerForSingleValueEvent(object :ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
                val fetch = p0.getValue(Sales::class.java)
          if (fetch == null){
              savepost()
          }else if (fetch.uid == myid){
                    val builder = CFAlertDialog.Builder(this@ViewSales)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Post Already exist")
                        .setTextGravity(Gravity.CENTER)
                        .setMessage("This post was previously saved")
                    builder.setHeaderView(R.layout.popduplicate)
                    builder.addButton(
                        "                    Alright                    ",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show();
                }else{
                    return
                }

        }
        override fun onCancelled(p0: DatabaseError) {

        }
    })
}


    private fun savepost() {
        Toast.makeText(this,"saving",Toast.LENGTH_SHORT).show()
        val myid = FirebaseAuth.getInstance().uid.toString()
        val filename = postpreview?.postcode
        val mysaves = FirebaseDatabase.getInstance().getReference("saves/$myid/$filename")
        val fullnames = postpreview?.fullnames.toString()
        val profileimageurl = postpreview?.profileImageUrl.toString()
        val email = postpreview?.email.toString()
        val postcode = postpreview?.postcode.toString()
        val homename = postpreview?.homename.toString()
        val location = postpreview?.location.toString()
        val propertytype = postpreview?.propertytype.toString()
        val rooms = postpreview?.rooms.toString()
        val bedrooms = postpreview?.bedrooms.toString()
        val bathrooms = postpreview?.bathrooms.toString()
        val furnishing = postpreview?.furnishing.toString()
        val price1 = postpreview?.price1.toString()
        val price2 = postpreview?.price2.toString()
        val abouthome = postpreview?.abouthome.toString()
        val img1 = postpreview?.image1.toString()
        val img2 = postpreview?.image2.toString()
        val img3 = postpreview?.image3.toString()
        val timestamp = postpreview?.timestamp.toString()
        val phone = postpreview?.phone.toString()
        val savedata = Sales(myid,fullnames,profileimageurl,email,postcode,homename,location,propertytype,rooms,bedrooms,bathrooms,furnishing,price1,price2,abouthome,img1,img2,img3,timestamp,phone)
        mysaves.setValue(savedata)
            .addOnSuccessListener {
                val builder = CFAlertDialog.Builder(this@ViewSales)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Added to Wishlist")
                    .setTextGravity(Gravity.CENTER)
                    .setMessage("View your saved posts at any time from your wishlist")
                builder.setHeaderView(R.layout.popwish)
                builder.addButton(
                    "                    Alright                    ",
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#429ef4"),
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.CENTER
                ) { dialog, which ->
                    dialog.dismiss()
                }
                builder.show();
                return@addOnSuccessListener
            }
    }

    private fun makePhoneCall(toId: String) : Boolean {
        try {
            val toId = postpreview?.phone
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$toId"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    private fun sendrequest() {
        val myid = FirebaseAuth.getInstance().uid
        val uuid = System.currentTimeMillis()
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

            val  ref= FirebaseDatabase.getInstance().getReference("users/$myid")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                   val  myprofile = p0.getValue(User::class.java)
                    if (myprofile!=null){
                        val myname = myprofile.fullnames
                        val profilepic = myprofile.profileImageUrl
                        val toid = postpreview?.uid
                        val property = postpreview?.homename
                        val myphone = myprofile.phone
                        val email = myprofile.email
                        val send = FirebaseDatabase.getInstance().getReference("requests/$uuid")
                        val sendrequest = Request(myid.toString(),myname,profilepic, toid.toString(),formatedDate, property.toString(),myphone,email)
                        send.setValue(sendrequest)
                            .addOnSuccessListener {
                                val builder = CFAlertDialog.Builder(this@ViewSales)
                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                                    .setTitle("Great!")
                                    .setTextGravity(Gravity.CENTER)
                                    .setMessage("Your request has been sent, the owner will contact you soon")
                                builder.setHeaderView(R.layout.pophurray)
                                builder.addButton(
                                    "                    Dismiss                    ",
                                    Color.parseColor("#FFFFFF"),
                                    Color.parseColor("#429ef4"),
                                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                                    CFAlertDialog.CFAlertActionAlignment.CENTER
                                ) { dialog, which ->
                                    dialog.dismiss()
                                }
                                builder.show();
                            }
                    }else{
                        return
                    }

                }
                override fun onCancelled(p0: DatabaseError) {
                    return
                }

            })


    }

    private fun email(){
        Toast.makeText(this,"redirecting...",Toast.LENGTH_SHORT).show()
        val email = postpreview?.email
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "$email", null
            )

        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Buying property")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "write your request in buying the property.."
        )
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }
    private fun displayproperty(){
        //..profile image
        val profileimage = findViewById<ImageView>(R.id.profilepic)
        Glide.with(this).load(postpreview?.profileImageUrl).into(profileimage)
        //....
        val housetype = findViewById<TextView>(R.id.saleshousetype)
        housetype.setText(postpreview?.homename)

        val location = findViewById<TextView>(R.id.homelocation)
        location.setText(postpreview?.location)
        //...
        val price1 = findViewById<TextView>(R.id.price1)
        val price2 = findViewById<TextView>(R.id.price2)
        price1.setText(postpreview?.price1)
        price2.setText(postpreview?.price2)

        val username = findViewById<TextView>(R.id.username)
        username.setText(postpreview?.fullnames)

        val email = findViewById<TextView>(R.id.salesemail)
        email.setText(postpreview?.email)

        val about = findViewById<TextView>(R.id.aboutthishome)
        about.setText(postpreview?.abouthome)

        val property = findViewById<TextView>(R.id.morepropertytypeavail)
        property.setText(postpreview?.propertytype)

        val bathrooms = findViewById<TextView>(R.id.bathroomscount)
        bathrooms.setText(postpreview?.bathrooms)

        val bedroom = findViewById<TextView>(R.id.bedroomscount)
        bedroom.setText(postpreview?.bedrooms)

        val furnish = findViewById<TextView>(R.id.furnishing)
        furnish.setText(postpreview?.furnishing)

        val rooms = findViewById<TextView>(R.id.rooms)
        rooms.setText(postpreview?.rooms)

    }


}