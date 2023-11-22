package com.example.shooping

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shooping.databinding.ActivityYoursaleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class yoursale : AppCompatActivity() {
    lateinit var bind: ActivityYoursaleBinding
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityYoursaleBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.orderlistText.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        bind.loadingOverlay.visibility = View.VISIBLE
        bind.loadingProgressBar.visibility = View.VISIBLE
        val id = auth.currentUser?.uid
        val userref = firestore.collection("Users").document(id.toString())
        val allDataCollectionRef = userref.collection("AllData")
        allDataCollectionRef.get().addOnSuccessListener {
            val array : MutableList<alldata> = mutableListOf()
            for (data in it.documents) {
                val Theid = data.id
                val product = data.getString("Productname")
                val rent = data.getString("Rent")
                val date = data.getString("date")
                val phonnumber = data.getString("Phonenumber")
                val ReasonTosale = data.getString("ReasonTosale")
                val details = data.getString("Details")
                val type = data.getString("Type")
                val uri1 = data.getString("pic1")
                val uri2 = data.getString("pic2")
                val mp = alldata(
                    product,
                    rent,
                    date,
                    phonnumber,
                    ReasonTosale,
                    details,
                    type,
                    uri1, uri2, Theid
                )
                array.add(mp)
            }
                bind.loadingProgressBar.visibility = View.GONE
                bind.loadingOverlay.visibility = View.GONE
                val recyclerView = bind.recycler
                recyclerView.layoutManager = LinearLayoutManager(this@yoursale)
                val Adpter = horizontaladpter(this@yoursale, array)
                recyclerView.adapter = Adpter

        }?.addOnFailureListener {
            Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show()
        }
    }
}

