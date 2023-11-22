package com.example.shooping

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class slideadapter(private val context: Context, private val imageList: Array<Int>) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.setImageResource(imageList[position])
        container.addView(imageView)
        return imageView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}