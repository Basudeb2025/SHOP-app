package com.example.shooping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.shooping.databinding.ActivityYourOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class yourOrder : AppCompatActivity() {
    lateinit var bind : ActivityYourOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityYourOrderBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.orderlistText.visibility = View.GONE
        bind.loadingOverlay.visibility = View.VISIBLE
        bind.loadingProgressBar.visibility = View.VISIBLE
        bind.loadingOverlay.visibility = View.GONE
        bind.loadingProgressBar.visibility = View.GONE
        bind.orderlistText.visibility = View.VISIBLE
    }
}