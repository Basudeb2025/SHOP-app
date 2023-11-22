package com.example.shooping

import android.net.Uri

//"Productname" to productname , "Rent" to Rent, "date" to date ,"Phonenumber" to phonenumber, "ReasonTosale" to reasontosale ,"Details" to details, "Type" to type , "Pic1" to picUri1, "Pic2" to picUri2
data class alldata(
    val productname: String?, val Rent: String?, val date:String ?, val phonenumber:String?,
    val Reason:String?,
    val details: String?,
    val type: String?, val uri1: String?,val uri2: String?,val Theid:String?)
