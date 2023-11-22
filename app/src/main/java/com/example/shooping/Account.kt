package com.example.shooping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var viewpager: ViewPager
private val imageList = arrayOf(R.drawable.sale_banner,R.drawable.sale_banner2,R.drawable.sale_banner3)
private var currentPage = 0
private val handler = android.os.Handler()
/**
 * A simple [Fragment] subclass.
 * Use the [Account.newInstance] factory method to
 * create an instance of this fragment.
 */
class Account : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var Name:String ?= null
    private var Email :String ?= null
    lateinit var databaseReference: DatabaseReference
    lateinit var progressBar: ProgressBar
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        auth = FirebaseAuth.getInstance()
        val current = auth.currentUser?.email.toString()
        var uniqe:String = ""
        for(i in current){
            if(i == '.' || i == ',' || i == '#' || i == '$' || i == '[' || i == ']') continue
            uniqe += i
        }
        uniqe += "Codeit"
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val name = view.findViewById<TextView>(R.id.profile_name)
        val email = view.findViewById<TextView>(R.id.profile_email)
        progressBar = view.findViewById(R.id.loadingProgressBar)
        progressBar.visibility = View.VISIBLE
        val pl = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        pl.visibility = View.VISIBLE
        val dp = view.findViewById<ImageView>(R.id.dp)
        val card = view.findViewById<CardView>(R.id.card)
        val sale = view.findViewById<CardView>(R.id.sale)
        val la = view.findViewById<RelativeLayout>(R.id.la)
        card.visibility = View.GONE
        sale.visibility = View.GONE
        la.visibility = View.GONE
        val lay = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        databaseReference.child(uniqe).get().addOnSuccessListener {
            if(it.exists()){
                Name = it.child("Name").value.toString()
                Email = it.child("Email").value.toString()
                name.text = Name
                email.text = Email
                lay.visibility = View.GONE
                progressBar.visibility = View.GONE
                card.visibility = View.VISIBLE
                sale.visibility = View.VISIBLE
                la.visibility = View.VISIBLE
//                dp.visibility = View.VISIBLE
//                name.visibility = View.VISIBLE
//                email.visibility = View.VISIBLE
            }
        }
        viewpager =  view.findViewById(R.id.viewPager)
        val adapter = slideadapter(context as Activity, imageList)
        viewpager.adapter = adapter
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == imageList.size) {
                        currentPage = 0
                    }
                    viewpager.currentItem = currentPage++
                }
            }
        }, 5000, 3000)
        val btn = view.findViewById<CardView>(R.id.sale)
        btn.setOnClickListener {
            startActivity(Intent(context,saleactivity::class.java))
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Account.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Account().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}