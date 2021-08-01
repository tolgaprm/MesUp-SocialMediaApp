package com.tolgapirim.mesup

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions

fun ImageView.gorselIndir(url:String?, placeholder: CircularProgressDrawable){
    val options = RequestOptions().placeholder(placeholder)
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}

fun placeholderYap(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth =8f
        centerRadius=40f
        start()
    }
}