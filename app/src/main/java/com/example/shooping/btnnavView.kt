package com.example.shooping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shooping.databinding.ActivityBtnnavViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class btnnavView : AppCompatActivity() {
    lateinit var bbind : ActivityBtnnavViewBinding
    lateinit var textView1: ImageView
    lateinit var textView2: TextView
    companion object{
        const val KEY1 = "com.example.shooping"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bbind = ActivityBtnnavViewBinding.inflate(layoutInflater)
        setContentView(bbind.root)
        textView1 = findViewById(R.id.welcome)
       replaceWithfrage(Home_frage())
       bbind.btnNavid.setOnItemSelectedListener {
           when(it.itemId){
               R.id.Home-> replaceWithfrage(Home_frage())
               R.id.account->replaceWithfrage(Account())
               R.id.Items->replaceWithfrage(ItmesFore())
               R.id.Menu->replaceWithfrage(menuFor())
               else->{
                   //No need to write anything
               }
           }
           true
       }

    }

    private fun replaceWithfrage(fragment: Fragment) {
       val fargementMana = supportFragmentManager
        val frgetrac = fargementMana.beginTransaction()
        frgetrac.replace(R.id.framebtn,fragment)
        frgetrac.commit()
    }

}
