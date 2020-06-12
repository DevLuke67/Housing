package com.studio.housing3.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studio.housing3.R
import com.studio.housing3.ViewSales
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.explore_layout.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import modules.Sales
import modules.User

class ExploreFragment : Fragment() {
    companion object{
        var USER = "USER"
    }
    var root:View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val profileimage = root?.findViewById(R.id.profilepicture) as ImageView
        val myid = FirebaseAuth.getInstance().uid
        val fetchprofile = FirebaseDatabase.getInstance().getReference("users/$myid")
        fetchprofile.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val me = p0.getValue(User::class.java)
                Glide.with(activity!!).load(me?.profileImageUrl).into(profileimage)
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        //...
        val search = root?.findViewById(R.id.search) as EditText
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchchat()
            }



        })
        ///...
        //....
            val ref = FirebaseDatabase.getInstance().getReference("/posts")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val adapter = GroupAdapter<ViewHolder>()
                    p0.children.forEach {
                        Log.d("Members", it.toString())

                        val user = it.getValue(Sales::class.java)
                        if (user != null) {
                            adapter.add(UserPost(user))

                        }

                    }
                    adapter.setOnItemClickListener { item, view ->
                        val userItem = item as UserPost
                        val intent = Intent(view.context, ViewSales::class.java)
                        intent.putExtra(USER, userItem.user)
                        startActivity(intent)
                    }
                    val rec = root?.findViewById(R.id.recyclerview_dashboard) as RecyclerView
                    rec.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
            //....


        return root

    }

    private fun searchchat() {
        val getid = root?.findViewById(R.id.search) as EditText
        val input = getid.text.toString()

        val keys = FirebaseDatabase.getInstance().getReference("/posts").orderByChild("location").startAt(input).endAt(input + "\uf8ff")

        keys.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user = it.getValue(Sales::class.java)
                    adapter.add(UserPost(user!!))

                }

                adapter.setOnItemClickListener{item, view ->
                    val userItem = item as UserPost
                    val intent = Intent(view.context, ViewSales::class.java)
                    intent.putExtra(USER, userItem.user)
                    startActivity(intent)
                }
                val rec = root?.findViewById(R.id.recyclerview_dashboard) as RecyclerView
                rec.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }


}
class UserPost(val user: Sales) :  Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.housename.text = user.homename
        viewHolder.itemView.housetype.text = user.propertytype
        viewHolder.itemView.salesusername.text = user.fullnames
        viewHolder.itemView.salespricemin.text = user.price2
        viewHolder.itemView.salelocation.text = user.location
        viewHolder.itemView.bedroomscount.text = user.bedrooms
        Glide.with(viewHolder.itemView).load(user.profileImageUrl).into(viewHolder.itemView.salesprofile)
        Glide.with(viewHolder.itemView).load(user.image2).into(viewHolder.itemView.imageone)


    }

    override fun getLayout(): Int {
        return R.layout.explore_layout
    }
}