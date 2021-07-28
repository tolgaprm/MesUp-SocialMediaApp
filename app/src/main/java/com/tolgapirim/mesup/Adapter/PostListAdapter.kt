package com.tolgapirim.mesup.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.gorselIndir
import com.tolgapirim.mesup.model.Post
import com.tolgapirim.mesup.placeholderYap
import kotlinx.android.synthetic.main.post_recycler_row.view.*
import kotlinx.android.synthetic.main.post_recycler_row.view.recyclerRowMesajText
import kotlinx.android.synthetic.main.post_recycler_row.view.recyclerRowprofilImages
import kotlinx.android.synthetic.main.post_recycler_row.view.recyclerRowuserName
import kotlinx.android.synthetic.main.post_recycler_row_with_images.view.*

class PostListAdapter(val postListesi:ArrayList<Post>):RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {

    val VIEW_TYPE_WITH_IMAGES = 1
    val VIEW_TYPE_WITHOUT_IMAGES = 2
    val VIEW_TYPE_JUST_IMAGE = 3


    class PostViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun getItemViewType(position: Int): Int {
        if(postListesi.get(position).postGorsel!="" && postListesi.get(position).mesaj=="" ){
            return VIEW_TYPE_JUST_IMAGE
        }else if(postListesi.get(position).postGorsel!=""){
            return VIEW_TYPE_WITH_IMAGES
        }
        else{
            return VIEW_TYPE_WITHOUT_IMAGES
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        if (viewType==1){
            val view  =LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row_with_images,parent,false)
            return PostViewHolder(view)
        }else if(viewType==3){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row_just_image,parent,false)
            return PostViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row,parent,false)
            return PostViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        if(postListesi.get(position).mesaj!=""){
            holder.itemView.recyclerRowMesajText.text = postListesi[position].mesaj
        }

        holder.itemView.recyclerRowuserName.text = postListesi.get(position).kullaniciAd
        
        if (postListesi[position].postGorsel!=""){
            holder.itemView.imageView4.gorselIndir(postListesi[position].postGorsel, placeholderYap(holder.itemView.context))
        }

        if(postListesi[position].profilResim!=""){
            holder.itemView.recyclerRowprofilImages.gorselIndir(postListesi[position].profilResim,
                placeholderYap(holder.itemView.context)
            )
        }

    }

    override fun getItemCount(): Int {
        return postListesi.size
    }
}