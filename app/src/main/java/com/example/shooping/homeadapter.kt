package com.example.shooping

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class homeadapter(val activity: Activity, val final: MutableList<MutableList<alldata>>):RecyclerView.Adapter<homeadapter.viewHolder>() {
    var col:Int = -1
    private lateinit var mylistener:onItemclickListener
    interface onItemclickListener{
        fun onItemclcik(row: Int,col:Int)
    }
    fun seontItemclicklistener(listener: onItemclickListener){
        mylistener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemforre,parent,false)
        return viewHolder(view,mylistener)
    }

    override fun getItemCount(): Int {
      return final.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
      val current : MutableList<alldata> = final[position]
        if(current.size == 2){
            holder.hname1.text = current[0].productname
            holder.hname2.text = current[1].productname
            holder.hprice1.text ="₹"+ current[0].Rent
            holder.hprice2.text = "₹"+ current[1].Rent
            Picasso.get().load(current[0].uri1).into(holder.hpic1)
            Picasso.get().load(current[1].uri2).into(holder.hpic2)
        }
      else if(current.size == 1){
            holder.hname1.text = current[0].productname
            holder.hprice1.text = current[0].Rent
            Picasso.get().load(current[0].uri1).into(holder.hpic1)
            holder.lay.visibility = View.GONE
        }

    }
    class viewHolder(val itemview: View,listener: onItemclickListener):RecyclerView.ViewHolder(itemview) {
        val hname1 = itemview.findViewById<TextView>(R.id.item1name)
        val hname2 = itemview.findViewById<TextView>(R.id.item2name)
        val hprice1 = itemview.findViewById<TextView>(R.id.item1price)
        val hprice2 = itemview.findViewById<TextView>(R.id.item2price)
        val hpic1 = itemview.findViewById<ImageView>(R.id.item1)
        val hpic2 = itemview.findViewById<ImageView>(R.id.item2)
        val hf = itemview.findViewById<LinearLayout>(R.id.lay1)
        val sf = itemview.findViewById<LinearLayout>(R.id.lay2)
        val lay = itemview.findViewById<LinearLayout>(R.id.lay2)
        init {
            hf.setOnClickListener {
                listener.onItemclcik(adapterPosition,0)
            }
            sf.setOnClickListener {
                listener.onItemclcik(adapterPosition,1)
            }
        }

    }
}