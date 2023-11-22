package com.example.shooping

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shooping.databinding.ActivitySignIBinding
import com.google.firebase.auth.FirebaseAuth

class signI : AppCompatActivity() {
    lateinit var sbind : ActivitySignIBinding
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sbind = ActivitySignIBinding.inflate(layoutInflater)
        setContentView(sbind.root)
        auth = FirebaseAuth.getInstance()
        sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.white))
        if(supportActionBar != null) supportActionBar?.hide()
        sbind.pageSignUp.setOnClickListener {
            startActivity(Intent(this,Sign_up::class.java))
            finish()
        }
        sbind.forgetpassword.setOnClickListener {
            startActivity(Intent(this,forgetpassword::class.java))
        }
        auth = FirebaseAuth.getInstance()
        sbind.SignInBtn.setOnClickListener {
            val em = sbind.EmailSignIn.text.toString()
            val pa = sbind.PasswordSignIn.text.toString()
            if (em.isNotEmpty() && pa.isNotEmpty()) {
                sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.white))
                auth.signInWithEmailAndPassword(em, pa).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Signin done", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this,btnnavView::class.java))
                    } else {
                        sbind.warning.text = "*user does not exist"
                        sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
                        Toast.makeText(this, "user does not exist", Toast.LENGTH_LONG).show()
                    }
                }.addOnCompleteListener {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            else if(em.isEmpty() && pa.isEmpty()){
                sbind.warning.text = "*Please enter your full details"
                sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            else if(em.isEmpty()){
                sbind.warning.text = "*Enter your email"
                sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            else{
                sbind.warning.text = "*Enter your password"
                sbind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }
}