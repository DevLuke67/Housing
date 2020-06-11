package com.studio.housing3.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.studio.housing3.R
import com.studio.housing3.ViewSales
import com.studio.housing3.ui.dashboard.ExploreFragment
import com.studio.housing3.ui.dashboard.UserPost
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.explore_layout.view.*
import modules.Sales

class WishlistFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_wish, container, false)
    val myid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("saves/$myid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("Members", it.toString())
                    val saves = it.getValue(Sales::class.java)
                    if (saves != null) {
                        adapter.add(UserPost(saves))

                    }

                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserPost
                    val intent = Intent(view.context, ViewSales::class.java)
                    intent.putExtra(ExploreFragment.USER, userItem.user)
                    startActivity(intent)
                }
                val rec = root.findViewById(R.id.wish_recyclerview) as RecyclerView
                rec.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        return root
    }

}
