package com.studio.housing3.ui.notifications

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studio.housing3.R
import com.studio.housing3.ViewSales
import com.studio.housing3.ui.dashboard.ExploreFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.notification_layout.view.*
import modules.Request
import modules.Sales


class FragmentNotify : Fragment() {
companion object{
    val USER_INFO = "USER_INFO"
}
var user:Request? = null
    var Info:Request? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_notification, container, false)
        val ref = FirebaseDatabase.getInstance().getReference("/requests")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    val myid = FirebaseAuth.getInstance().uid
                    Log.d("Members", it.toString())

                    user = it.getValue(Request::class.java)
                    if (user?.toid == myid) {
                        adapter.add(UserPost(user!!))
                    }else{
                        return@forEach
                    }

                }

                val rec = root.findViewById(R.id.recyclerview_notification) as RecyclerView
                rec.adapter = adapter
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserPost
                    val intent = Intent(view.context, FragmentNotify::class.java)
                    intent.putExtra(USER_INFO, userItem.user)
                    Info = intent.getParcelableExtra<Request>(USER_INFO)



                    val builder = CFAlertDialog.Builder(activity)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Reply with")
                        .setTextGravity(Gravity.CENTER)
                    builder.addButton(
                        "                          Call                          ",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        fun makePhoneCall(toId: String) : Boolean {
                            try {
                                val toId = Info?.mphone
                                val intent = Intent(Intent.ACTION_CALL)
                                intent.setData(Uri.parse("tel:$toId"))
                                startActivity(intent)
                                return true
                            } catch (e: Exception) {
                                e.printStackTrace()
                                return false
                            }
                        }
                        makePhoneCall(toString())
                        dialog.dismiss()
                    }
                    builder.addButton(
                        "                     Whatsapp                    ",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->

                        val phone = Info?.mphone
                        openNewTabWindow("https://api.whatsapp.com/send?phone=$phone&text=Your+request+has+been+received",requireActivity())

                        dialog.dismiss()
                    }
                    builder.addButton(
                        "                        Email                         ",
                        Color.parseColor("#FFFFFF"),
                        Color.parseColor("#429ef4"),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER
                    ) { dialog, which ->
                        val email = Info?.memail
                        val emailIntent = Intent(
                            Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "$email", null
                            )

                        )
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reply to your request")
                        emailIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Reply to this request"
                        )
                        startActivity(Intent.createChooser(emailIntent, "Send Email"))

                        dialog.dismiss()
                    }
                    builder.show();
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return root


    }
    fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context.startActivity(intents)
    }
    private fun makePhoneCall(toId: String) : Boolean {
        try {
            val toId = Info?.mphone
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$toId"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    private fun whatsapp(){
        val phone = user?.mphone
        openNewTabWindow("https://api.whatsapp.com/send?phone=$phone&text=I'm+interested+in+buying+your+home",requireActivity())
    }
private fun itmesclicked(){




}
}
class UserPost(val user: Request) :  Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

    Glide.with(viewHolder.itemView).load(user.profilepic).into(viewHolder.itemView.reqprofile)
    viewHolder.itemView.nusername.text = user.fullname
        viewHolder.itemView.nphone.text = user.mphone
        viewHolder.itemView.nproperty.text = user.property
        viewHolder.itemView.nemail.text = user.memail
    }

    override fun getLayout(): Int {
        return R.layout.notification_layout
    }
}