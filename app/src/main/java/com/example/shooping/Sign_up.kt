package com.example.shooping

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shooping.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Sign_up : AppCompatActivity() {
    lateinit var bind : ActivitySignUpBinding
    lateinit var au : FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
        bind = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(bind.root)
        au = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        if(supportActionBar != null){
            supportActionBar ?.hide()
        }
        bind.passwordSignUp.setOnClickListener {
            Toast.makeText(this,"Set password for signIn",Toast.LENGTH_LONG).show()
        }
        bind.SignUpBtn.setOnClickListener {
            var uniqe :String = ""
            val name = bind.NameSignUp.text.toString()
            val em = bind.EmailSignUp.text.toString()
            val pas = bind.passwordSignUp.text.toString()
            for(i in em){
                if(i == '.' || i == ',' || i == '#' || i == '$' || i == '[' || i == ']') continue
                uniqe += i
            }
            uniqe += "Codeit"
            val check:Boolean = Elig(pas)
            if(name.isNotEmpty() && em.isNotEmpty() && check){
                val current = pas+"thisIstheandroid"
                val map = mapOf("Name" to name,"Email" to em,"password" to pas)
                databaseReference.child(uniqe).setValue(map).addOnCompleteListener {
                    //Message for succes
                }.addOnFailureListener {
                   // Message for failure
                }
                 au.createUserWithEmailAndPassword(em,pas).addOnCompleteListener {
                     if(it.isSuccessful) {
                         Toast.makeText(this, "Complete the sign up", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,btnnavView::class.java)
                         startActivity(intent)
                     }
                     else{
                         bind.warning .text = "*Fialed, Please try again"
                         bind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
                     }
                 }.addOnFailureListener {
                     bind.warning .text = "*Something went wrong"
                     bind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
                 }
            }
            else if(name.isNotEmpty() && em.isNotEmpty() && !check){
                bind.warning.text = "*Password will must contain atleast one digit,one speacial char and 4 length"
                bind.warning.setTextColor(ContextCompat.getColor(this,R.color.red))
            }
            else{
                bind.warning .text = "*Enter the full details"
                bind.warning.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }
     private fun Elig(password: String): Boolean {
         var special = false
         var num = false

         for (i in password) {
             if (i == '#' || i == '@' || i == '$' || i == '%' || i == '^' || i == '&' || i == '*') {
                 special = true
             } else if (i.isDigit()) {
                 num = true
             }
         }

         return password.length >= 4 && special && num
    }
}