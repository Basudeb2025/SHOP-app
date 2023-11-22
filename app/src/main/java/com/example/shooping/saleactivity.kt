package com.example.shooping

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shooping.databinding.ActivitySaleactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class saleactivity : AppCompatActivity() {
    lateinit var bind:ActivitySaleactivityBinding
    lateinit var auth : FirebaseAuth
    lateinit var firebaseDatabase : DatabaseReference
    lateinit var firebaseFirestore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = "pick_image_request"
    var picUri1 :Uri ?= null
    var picUri2:Uri ?= null
    var select1 : Boolean = false
    var select2 : Boolean = false
    var fnupload:Boolean = false
    var bkupload:Boolean = false
    lateinit var back : ImageView
    lateinit var piclink1:String
    lateinit var piclink2:String
    lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySaleactivityBinding.inflate(layoutInflater)
        setContentView(bind.root)
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        bind.proges1.visibility = View.GONE
        bind.proges2.visibility = View.GONE
        storage = FirebaseStorage.getInstance()
        bind.backbtn.setOnClickListener {
            onBackPressed()
        }
        bind.sbtbtn.setOnClickListener {
            startActivity(Intent(this,viewproduct::class.java))
        }
        bind.calernderimg.setOnClickListener {
            openDatePicker()
        }
        back = bind.backpic
        var chbtn1 = false
        var chbtn2 = false
        var type: String? = null
        bind.Radiogrp.setOnCheckedChangeListener{group,checkid->
            when(checkid){
                R.id.rgrp1->{
                  chbtn1 = true
                    type = "Electronics"
                }
                R.id.rgrp2->{
                    chbtn2 = true
                    type = "Others"
                }
            }
        }
       bind.frontpic.setOnClickListener {
          openImagePicker()
           select1 = true
       }
        bind.backpic.setOnClickListener {
            openImagePicker()
            select2 = true
        }
        val uniqeid = generateUniqueFileName()
        bind.ftntupload.setOnClickListener {
            if(fnupload){
                Toast.makeText(this,"Already uploaded",Toast.LENGTH_LONG).show()
            }
            else if(select1) {
                bind.proges1.visibility = View.VISIBLE
                uploadpic1(uniqeid)
            }
            else{
                Toast.makeText(this,"Select the pic first",Toast.LENGTH_SHORT).show()
            }
        }
        bind.bckupload.setOnClickListener {
            if(bkupload){
                Toast.makeText(this,"Already uploaded",Toast.LENGTH_LONG).show()
            }
           else if(select2){
                bind.proges2.visibility = View.VISIBLE
                uploadpic2(uniqeid)
            }
            else{
                Toast.makeText(this,"Select the pic first",Toast.LENGTH_SHORT).show()
            }
        }
        val alert = Dialog(this)
        alert.setContentView(R.layout.dialogcustom)
        bind.sbtbtn.setOnClickListener {
            val productname = bind.productname.text.toString()
            val Rent = bind.rent.text.toString()
            val date = bind.dateEditText.text.toString()
            val phonenumber = bind.phnumber.text.toString()
            val reasontosale = bind.Reasonsale.text.toString()
            val details = bind.detals.text.toString()
            val hintcolor = Color.RED
            if(productname.isEmpty()){
               bind.prolay.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
            }
            else{
                bind.prolay.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
            }
            if(Rent.isEmpty()){
               bind.relay.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
            }
            else{
                bind.relay.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
            }
            if(date.isEmpty()){
                bind.dtelay.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
            }
            else{
                bind.dtelay.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
            }
            if(phonenumber.isEmpty() || phonenumber.length != 10){
                Toast.makeText(this,"Enter a valid phone number",Toast.LENGTH_SHORT)
                bind.phlay.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
            }
            else{
                bind.phlay.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
            }
            if(details.isEmpty()){
               bind.detlay.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
            }
            else{
                bind.detlay.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
            }
            if(!fnupload){
                Toast.makeText(this,"Please upload pic 1",Toast.LENGTH_SHORT).show()
            }
            if(!bkupload){
                Toast.makeText(this,"Please upload pic 2",Toast.LENGTH_SHORT).show()
            }
            if(!chbtn1 && !chbtn2){
                bind.rgrp1.setTextColor(hintcolor)
                bind.rgrp2.setTextColor(hintcolor)
            }
            else{
                bind.rgrp1.setTextColor(Color.GREEN)
                bind.rgrp2.setTextColor(Color.GREEN)
            }
            if(bind.checkBox.isChecked){
                bind.checkBox.setTextColor(Color.GREEN)
            }
            else{
                bind.checkBox.setTextColor(Color.RED)
            }
            if(productname.isNotEmpty() && Rent.isNotEmpty() && date.isNotEmpty() && phonenumber.isNotEmpty() && reasontosale.isNotEmpty() && details.isNotEmpty() && phonenumber.length == 10 && bind.checkBox.isChecked && !fnupload && type != null && !bkupload){
                Toast.makeText(this,"Please wait untill picture are upload",Toast.LENGTH_LONG).show()
            }
            if(productname.isNotEmpty() && Rent.isNotEmpty() && date.isNotEmpty() && phonenumber.isNotEmpty() && reasontosale.isNotEmpty() && details.isNotEmpty() && phonenumber.length == 10 && bind.checkBox.isChecked && fnupload && type != null && bkupload){
                val map = mapOf("Productname" to productname , "Rent" to Rent, "date" to date ,"Phonenumber" to phonenumber, "ReasonTosale" to reasontosale ,"Details" to details, "Type" to type , "pic1" to piclink1, "pic2" to piclink2)
                val currentuser = auth.currentUser?.uid
                val userref = firebaseFirestore.collection("Users").document(currentuser.toString())
                val reference = userref.collection("AllData")
                reference.add(map).addOnSuccessListener {
                    alert.show()
                    Handler().postDelayed({
                        alert.dismiss()
                        onBackPressed()
                    },2000)
                    Toast.makeText(this,"Succes to add",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failure to add",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                // Check if imageUri1 is null; if so, assign uri to imageUri1; otherwise, assign to imageUri2
                if (picUri1 == null) {
                    picUri1 = uri
                } else {
                    picUri2 = uri
                }
            }
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        getContent.launch(intent)
    }
    private fun generateUniqueFileName(): String {
        val uniqueId = UUID.randomUUID().toString()
        return "pic_$uniqueId.jpg"
    }
    private fun uploadpic1(uniqeid: String) {
        if(auth.currentUser?.uid != null){
            val storageref = storage.getReference("Pics").child(auth.currentUser?.uid!!)
            val ref = storageref.child(uniqeid).child("pic1")
            val fin = ref.putFile(picUri1!!)
            fin.addOnSuccessListener {
                 ref.downloadUrl.addOnSuccessListener {
                     val link = it.toString()
                     piclink1 = link
                     bind.proges1.visibility = View.GONE
                     Toast.makeText(this,"Complete the first pic",Toast.LENGTH_LONG).show()
                     fnupload = true
                 }.addOnFailureListener {
                     Toast.makeText(this,"Failed to get the link",Toast.LENGTH_LONG).show()
                 }
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to save the link",Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun uploadpic2(uniqeid: String) {
        val userId = auth.currentUser?.uid
        if(userId != null){
            val storagref = storage.getReference("Pics").child(userId)
            val ref = storagref.child(uniqeid).child("pic2")
            val fin = ref.putFile(picUri2!!)
            fin.addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val link = it.toString()
                    piclink2 = link
                    bind.proges2.visibility = View.GONE
                    Toast.makeText(this,"Complete the second pic",Toast.LENGTH_LONG).show()
                    bkupload = true
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed to get the link",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to Save the link",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openDatePicker() {
        val cal = Calendar.getInstance()
        val date = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this,
            {
                    _,year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                bind.dateEditText.setText(selectedDate)
            },year,month,date
            )
        datePickerDialog.show()
    }



//    companion object{
//        private const val PICK_IMAGE_REQUEST1 = 1
//        private const val PICK_IMAGE_REQUEST2 = 2
//    }
// private fun saveimage(id:String, uri1:Uri, uri2 : Uri){
//    val user = auth.currentUser?.uid
//    val storageRef = FirebaseStorage.getInstance().reference
//    val imagesRef = user?.let { storageRef.child("images").child(it).child(id) }
//    // Save Pic1
//    if (uri1 != null) {
//        val pic1Ref = imagesRef?.child("pic1.jpg")
//        pic1Ref?.putFile(uri1!!)
//            ?.addOnSuccessListener {
//                // Pic1 upload successful
//                Toast.makeText(this, "Pic1 uploaded successfully", Toast.LENGTH_SHORT).show()
//            }
//            ?.addOnFailureListener { e ->
//                // Handle Pic1 upload failure
//                Toast.makeText(this, "Pic1 upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//    // Save Pic2
//    if (uri2 != null) {
//        val pic2Ref = imagesRef?.child("pic2.jpg")
//        pic2Ref?.putFile(uri2)
//            ?.addOnSuccessListener {
//                // Pic2 upload successful
//                Toast.makeText(this, "Pic2 uploaded successfully", Toast.LENGTH_SHORT).show()
//            }
//            ?.addOnFailureListener { e ->
//                // Handle Pic2 upload failure
//                Toast.makeText(this, "Pic2 upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
// }
}
