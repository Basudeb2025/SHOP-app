package com.example.shooping

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [menuFor.newInstance] factory method to
 * create an instance of this fragment.
 */
class menuFor : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var auth : FirebaseAuth
    lateinit var databaseReference: DatabaseReference
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
        auth = FirebaseAuth.getInstance()
        var uniqe :String = ""
        val email = auth.currentUser?.email.toString()
        for(i in email){
            if(i == '.' || i == ',' || i == '#' || i == '$' || i == '[' || i == ']') continue
            uniqe += i
        }
        uniqe += "Codeit"
        val view =  inflater.inflate(R.layout.fragment_menu_for, container, false)
        val prolay = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        val process = view.findViewById<ProgressBar>(R.id.loadingProgressBar)
        val legal = view.findViewById<Button>(R.id.legal)
        val about = view.findViewById<Button>(R.id.about)
        val btn = view.findViewById<Button>(R.id.logout)
        val name = view.findViewById<TextView>(R.id.name)
        val dp = view.findViewById<ImageView>(R.id.dp)
        process.visibility = View.VISIBLE
        prolay.visibility = View.VISIBLE
        legal.visibility = View.GONE
        about.visibility = View.GONE
        btn.visibility = View.GONE
        name.visibility = View.GONE
        dp.visibility = View.GONE
        val alter = context?.let { Dialog(it) }
        alter?.setContentView(R.layout.aboutlayout)
        about.setOnClickListener {
            alter?.show()
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        databaseReference.child(uniqe).get().addOnSuccessListener {
            if(it.exists()){
                val username = it.child("Name").value.toString()
                name.text = "Hi, "+ username
                process.visibility = View.GONE
                prolay.visibility = View.GONE
                legal.visibility = View.VISIBLE
                about.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
                btn.visibility = View.VISIBLE
                dp.visibility = View.VISIBLE
            }
        }
        btn.setOnClickListener {
            val btn = view.findViewById<Button>(R.id.logout)
            val notfication = AlertDialog.Builder(context)
            notfication.setIcon(R.drawable.baseline_add_alert_24)
            notfication.setTitle("Are you sure?")
            notfication.setMessage("Do you want to logout")
            notfication.setPositiveButton("yes",DialogInterface.OnClickListener { dialog, which ->
                auth.signOut()
                startActivity(Intent(context,signI::class.java))
            })
            notfication.setNegativeButton("no",DialogInterface.OnClickListener { dialog, which ->
                //For stay on that page
            })
            notfication.show()
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
         * @return A new instance of fragment menuFor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            menuFor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}