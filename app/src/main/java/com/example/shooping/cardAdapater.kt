package com.example.shooping

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
lateinit var databaseReference: DatabaseReference
lateinit var auth: FirebaseAuth
class cardAdapater(private val activity: Activity , private val array: MutableList<carddata>):RecyclerView.Adapter<cardAdapater.viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cardAdapater.viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.cardlayoutitem,parent,false))
    }

    override fun onBindViewHolder(holder: cardAdapater.viewholder, position: Int) {
        val current = array[position]
        holder.hname.text = current.productname
        holder.hrent.text = "₹"+current.Rent
        Picasso.get().load(current.uri1).into(holder.hImage)
        holder.hview.setOnClickListener {
            val intent = Intent(activity,viewproduct::class.java)
            intent.putExtra("name",current.productname)
            intent.putExtra("pic1",current.uri1)
            intent.putExtra("pic2",current.uri2)
            intent.putExtra("rent",current.Rent)
            intent.putExtra("details",current.details)
            intent.putExtra("reason",current.Reason)
            //To Give the alert to the viewproduct
            val alert:String = "card"
            intent.putExtra("rechived",alert)
            activity.startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
        holder.hremove.setOnClickListener {
            val user = auth.currentUser?.uid
            val id = current.itemid
            databaseReference = FirebaseDatabase.getInstance().getReference("cardData")
            val ref = databaseReference.child(user.toString()).child(id.toString())
            val alert = AlertDialog.Builder(activity)
            alert.setIcon(R.drawable.baseline_delete_24)
            alert.setTitle("Are you sure?")
            alert.setMessage("Do you want to remove this item")
            alert.setPositiveButton("No", DialogInterface.OnClickListener{ dialog, which->
                //The postion button, right side
            })
            alert.setNegativeButton("yes",DialogInterface.OnClickListener{dialog,which->
                ref.removeValue()
            })
            alert.show()
        }
    }
    override fun getItemCount(): Int {
       return array.size
    }
    class viewholder(val itemview: View):RecyclerView.ViewHolder(itemview) {
        val hImage = itemView.findViewById<ImageView>(R.id.image)
        val hrent = itemview.findViewById<TextView>(R.id.rent)
        val hname = itemview.findViewById<TextView>(R.id.name)
        val hremove = itemview.findViewById<TextView>(R.id.remove)
        val hview = itemview.findViewById<TextView>(R.id.viewitem)
    }


}