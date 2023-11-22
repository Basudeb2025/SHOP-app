package com.example.shooping

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shooping.databinding.ActivityForgetpasswordBinding
import com.google.firebase.auth.FirebaseAuth

class forgetpassword : AppCompatActivity() {
    lateinit var bind:ActivityForgetpasswordBinding
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(bind.root)
        auth = FirebaseAuth.getInstance()
        val alert = AlertDialog.Builder(this)
        alert.setTitle("The link has send to your email")
        alert.setMessage("Now you can set your new password by this link")
        alert.setPositiveButton("ok",DialogInterface.OnClickListener { dialogInterface, i ->
            //For postive button
        })
        bind.button.setOnClickListener {
            val email = bind.editTextTextPersonName.text.toString()
            if(email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    alert.show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Please check your email address", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else{
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}