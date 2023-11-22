package com.example.shooping

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.shooping.databinding.ActivityViewproductBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class viewproduct : AppCompatActivity() {
    lateinit var bind:ActivityViewproductBinding
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var imageL = arrayOf(R.drawable.buy,R.drawable.sale_karo,R.drawable.profit,R.drawable.profit_lo)
    private var imageList = arrayOf(R.drawable.buy,R.drawable.sale_karo)
    private var currentPage = 0
    private val handler = android.os.Handler()
    lateinit var auth:FirebaseAuth
    private lateinit var indicatorLayout: LinearLayout
    var  listofcollectimage : MutableList<String> = mutableListOf()
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityViewproductBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.scroll.visibility = View.GONE
        bind.progeslayout.visibility = View.VISIBLE
        bind.progess.visibility = View.VISIBLE
        //check we are come from where
        val ch = intent.getStringExtra("rechived")
        if(ch == "card"){
            bind.layout.visibility = View.GONE
            bind.cardbtn.visibility = View.VISIBLE
        }
        else{
            bind.cardbtn.visibility = View.GONE
            bind.layout.visibility = View.VISIBLE
        }
        auth = FirebaseAuth.getInstance()
//        bind.laout.visibility = View.GONE
//        bind.name.visibility = View.GONE
//        bind.price.visibility = View.GONE
//        bind.details.visibility = View.GONE
//        bind.reason.visibility = View.GONE
//        viewPager.visibility = View.GONE
        //Dialog for show the succes in the scren
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.carddialog)
        val bn = dialog.findViewById<Button>(R.id.gocardbtn)
        bn.setOnClickListener {
            //Go to the card section
            startActivity(Intent(this,yourcard::class.java))
        }
        // For show the progees bar
        Handler().postDelayed({
            val name = intent.getStringExtra("name")
            val rent = intent.getStringExtra("rent")
            val details = intent.getStringExtra("details")
            val reason = intent.getStringExtra("reason")
            val pic1 = intent.getStringExtra("pic1")
            val pic2 = intent.getStringExtra("pic2")
            bind.name.text = name
            bind.price.text = "₹"+rent
            bind.details.text = details
            bind.reason.text = reason
            if (pic1 != null) {
                listofcollectimage.add(pic1)
            }
            if (pic2 != null) {
                listofcollectimage.add(pic2)
            }
            bind.progeslayout.visibility = View.GONE
            bind.progess.visibility = View.GONE
            bind.scroll.visibility = View.VISIBLE
        },2000)
        bind.adtocard.setOnClickListener {
            Toast.makeText(this,"Item added to card",Toast.LENGTH_SHORT).show()
            val name = intent.getStringExtra("name")
            val rent = intent.getStringExtra("rent")
            val details = intent.getStringExtra("details")
            val reason = intent.getStringExtra("reason")
            val pic1 = intent.getStringExtra("pic1")
            val pic2 = intent.getStringExtra("pic2")
            val mp = mapOf("Productname" to name, "Rent" to rent , "Details" to details,"ReasonTosale" to reason, "Pic1" to pic1,"Pic2" to pic2)
            databaseReference = FirebaseDatabase.getInstance().getReference("cardData")
            val currentUser = auth.currentUser?.uid
            val uniqeId = databaseReference.push().key
            databaseReference.child(currentUser.toString()).child(uniqeId.toString()).setValue(mp).addOnSuccessListener {
                dialog.show()
            }.addOnFailureListener {
                Toast.makeText(this,"Sorry its failed to add, try again",Toast.LENGTH_LONG).show()
            }
        }
        viewPager = findViewById(R.id.viewPagr)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        val adapter= slideadap(this, listofcollectimage,imageList)
        viewPager.adapter = adapter
        addDotsIndicator(0)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                addDotsIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        val pager = bind.viewPager
        val adapte = slideadapter(this, imageL)
        pager.adapter = adapte
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == imageL.size) {
                        currentPage = 0
                    }
                    pager.currentItem = currentPage++
                }
            }
        }, 3000, 2000)
        val btn = findViewById<ImageView>(R.id.backbtn)
        btn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun addDotsIndicator(position: Int) {
        indicatorLayout.removeAllViews()
        val dots = arrayOfNulls<ImageView>(listofcollectimage.size)
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val widthHeight = 15 // Adjust the width and height of the dots
            val params = LinearLayout.LayoutParams(
                widthHeight, widthHeight
            )
            params.setMargins(10, 0, 10, 0)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(
                if (i == position) R.drawable.baseline_arrow_drop_down_circle_24 else R.drawable.baseline_arrow_drop_up_24
            )
            indicatorLayout.addView(dots[i])
        }
    }
}

