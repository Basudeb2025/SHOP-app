package com.example.shooping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.shooping.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var bind : ActivityMainBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        if(supportActionBar != null){
            supportActionBar?.hide()
        }
        auth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            val ani = AnimationUtils.loadAnimation(this,R.anim.anime)
            bind.logo.startAnimation(ani)
        },300
        )
        Handler().postDelayed({
            if(auth.currentUser != null) {
                startActivity(Intent(this,btnnavView::class.java))
            }
            else  startActivity(Intent(this,signI::class.java))
            finish()
            },3000

        )
    }
}