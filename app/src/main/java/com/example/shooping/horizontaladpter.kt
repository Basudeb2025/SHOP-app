package com.example.shooping

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.io.InputStream
lateinit var firebaseFirestore: FirebaseFirestore
class horizontaladpter(val context: Activity, val array: MutableList<alldata>
):RecyclerView.Adapter<horizontaladpter.viewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): horizontaladpter.viewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.orderlist,parent,false)
        return viewHolder(view)
    }
    override fun onBindViewHolder(holder: horizontaladpter.viewHolder, position: Int) {
       val current = array[position]
        holder.hname .text = current.productname
        holder.hdetails.text = "₹"+current.Rent
        Picasso.get().load(current.uri1).into(holder.pic)
        holder.delet.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            firebaseFirestore = FirebaseFirestore.getInstance()
            val currentuser = auth.currentUser?.uid
            val userref = firebaseFirestore.collection("Users").document(currentuser.toString())
            val ref = userref.collection("AllData")
            val dltref = ref.document(current.Theid.toString())
            val alert = AlertDialog.Builder(context)
            alert.setIcon(R.drawable.baseline_delete_forever_24)
            alert.setMessage("Do you want to delete it")
            alert.setTitle("Are you sure?")
            alert.setPositiveButton("No",DialogInterface.OnClickListener{ dialog, which->
                //No
            })
            alert.setNegativeButton("yes",DialogInterface.OnClickListener{dialog,which->
                  dltref.delete().addOnSuccessListener {
                      //Succes the delete
                      Toast.makeText(context,"Delete it",Toast.LENGTH_LONG).show()
                  }.addOnFailureListener {
                      //Failed
                  }
            })
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return array.size
    }
    class viewHolder(itemview:View):RecyclerView.ViewHolder(itemview) {
      val hname = itemview.findViewById<TextView>(R.id.name)
        val hdetails = itemview.findViewById<TextView>(R.id.details)
        val pic = itemview.findViewById<ImageView>(R.id.pic)
        val delet = itemview.findViewById<ImageView>(R.id.delete)
    }
}
