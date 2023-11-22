package com.example.shooping

import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home_frage.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home_frage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var auth : FirebaseAuth
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var imageL = arrayOf(R.drawable.banner_for_shop,R.drawable.everything,R.drawable.electronics, R.drawable.img_15)
    private var currentPage = 0
    private val handler = android.os.Handler()
    private lateinit var indicatorLayout: LinearLayout
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
        //Firebase
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
       val view =  inflater.inflate(R.layout.fragment_home_frage, container, false)
        progressBar = view.findViewById(R.id.loadingProgressBar)
        val lay = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        val slidelayout = view.findViewById<RelativeLayout>(R.id.viewpagerlayout)
        //for FCM
        FirebaseMessaging.getInstance().subscribeToTopic("FCMnoti")
        slidelayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        lay.visibility = View.VISIBLE
        val user = auth.currentUser?.uid
        val allDataCollectionRef = firebaseFirestore.collectionGroup("AllData")
        allDataCollectionRef.get().addOnSuccessListener {
            val array : MutableList<alldata> = mutableListOf()
            for(data in it.documents) {
                val product = data.getString("Productname")
                val rent = data.getString("Rent")
                val date = data.getString("date")
                val phonnumber = data.getString("Phonenumber")
                val ReasonTosale = data.getString("ReasonTosale")
                val details = data.getString("Details")
                val type = data.getString("Type")
                val picurl1 = data.getString("pic1")
                val picuel2 = data.getString("pic2")
                val mp = alldata(
                    product,
                    rent,
                    date,
                    phonnumber,
                    ReasonTosale,
                    details,
                    type,
                    picurl1,
                    picuel2,""
                )
                array.add(mp)
            }
            val final :MutableList<MutableList<alldata>> = mutableListOf()
            var temp:MutableList<alldata> = mutableListOf()
            for(i in array.indices){
                temp.add(array[i])
                if(temp.size == 2 || i == array.size-1){
                    final.add(temp)
                    temp = mutableListOf()
                }
            }
                progressBar.visibility = View.GONE
                lay.visibility = View.GONE
                slidelayout.visibility = View.VISIBLE
                recyclerView = view.findViewById(R.id.recycle)
                val adp = homeadapter(context as Activity,final)
                recyclerView.adapter = adp
                recyclerView.layoutManager = LinearLayoutManager(context)
                adp.seontItemclicklistener(object :homeadapter.onItemclickListener{
                    override fun onItemclcik(row:Int,col:Int) {
                        val intent = Intent(context,viewproduct::class.java)
                        val name = final[row][col].productname
                        val rent = final[row][col].Rent
                        val fpic = final[row][col].uri1
                        val bpic = final[row][col].uri2
                        val details = final[row][col].details
                        val reason = final[row][col].Reason
                        intent.putExtra("name",name)
                        intent.putExtra("rent",rent)
                        intent.putExtra("pic1",fpic)
                        intent.putExtra("pic2",bpic)
                        intent.putExtra("details",details)
                        intent.putExtra("reason",reason)
                        //To give the alert
                        val alert:String = "home"
                        intent.putExtra("rechived",alert)
                        startActivity(intent)
                    }
                })
            }.addOnFailureListener {
            Toast.makeText(context,"Failure",Toast.LENGTH_LONG).show()
        }
        viewPager = view.findViewById(R.id.viewPager)
        indicatorLayout = view.findViewById(R.id.indicatorLayout)
        val adapter = slideadapter(context as Activity,imageL)
        viewPager.adapter = adapter
        addDotsIndicator(0)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                addDotsIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewpager =  view.findViewById(R.id.viewPager)
        val adpter = slideadapter(context as Activity, imageL)
        viewpager.adapter = adpter
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == imageL.size) {
                        currentPage = 0
                    }
                    viewpager.currentItem = currentPage++
                }
            }
        }, 5000, 4000)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home_frage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home_frage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun addDotsIndicator(position: Int) {
        val context = context ?: return
        indicatorLayout.removeAllViews()
        val dots = arrayOfNulls<ImageView>(imageL.size)
        for (i in dots.indices) {
            dots[i] = ImageView(context)
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
