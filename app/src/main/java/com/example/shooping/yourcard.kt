package com.example.shooping

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shooping.databinding.ActivityYourcard2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
class yourcard : AppCompatActivity() {
    lateinit var bind:ActivityYourcard2Binding
    lateinit var databaseReference: DatabaseReference
    lateinit var adapter : cardAdapater
    lateinit var auth : FirebaseAuth
    private val array = mutableListOf<carddata>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityYourcard2Binding.inflate(layoutInflater)
        setContentView(bind.root)
        auth = FirebaseAuth.getInstance()
        bind.progeslayout.visibility = View.VISIBLE
        bind.progess.visibility = View.VISIBLE
        databaseReference = FirebaseDatabase.getInstance().getReference("cardData")
        val reference = databaseReference.child(auth.currentUser?.uid.toString())
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for(data in snapshot.children){
                    val name = data.child("Productname").getValue(String::class.java)
                    val rent = data.child("Rent").getValue(String::class.java)
                    val pic1 = data.child("Pic1").getValue(String::class.java)
                    val pic2 = data.child("Pic2").getValue(String::class.java)
                    val reason = data.child("ReasonTosale").getValue(String::class.java)
                    val details = data.child("Details").getValue(String::class.java)
                    val id = data.key.toString()
                    val mp = carddata(name,rent,reason,details,pic1,pic2,id)
                    array.add(mp)
                }
                bind.progeslayout.visibility = View.GONE
                bind.progess.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
               //The error massege
            }

        })
        val recyclerView = bind.recycler
        adapter = cardAdapater(this as Activity,array)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}