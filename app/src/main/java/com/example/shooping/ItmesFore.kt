package com.example.shooping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ItmesFore.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItmesFore : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewpager: ViewPager
    private val imageist = arrayOf(R.drawable.img_18,R.drawable.img_16,R.drawable.img_17)
    private var currentPage = 0
    private val handler = android.os.Handler()

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
        val view = inflater.inflate(R.layout.fragment_itmes_fore, container, false)
        viewpager =  view.findViewById(R.id.viewPager)
        val orbtn = view.findViewById<Button>(R.id.orderbtn)
        val salebtn = view.findViewById<Button>(R.id.salebtn)
        val cardbtn = view.findViewById<Button>(R.id.cardbtn)
        orbtn.setOnClickListener {
            startActivity(Intent(context,yourOrder::class.java))
        }
        salebtn.setOnClickListener {
            startActivity(Intent(context,yoursale::class.java))
        }
        cardbtn.setOnClickListener {
            startActivity(Intent(context,yourcard::class.java))
        }
        val adapter = slideadapter(context as Activity, imageist)
        viewpager.adapter = adapter
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == imageist.size) {
                        currentPage = 0
                    }
                    viewpager.currentItem = currentPage++
                }
            }
        }, 3000, 2000)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItmesFore.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItmesFore().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}