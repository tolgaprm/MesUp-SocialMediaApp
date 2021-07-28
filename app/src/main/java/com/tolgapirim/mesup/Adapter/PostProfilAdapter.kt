package com.tolgapirim.mesup.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.gorselIndir
import com.tolgapirim.mesup.model.Post
import com.tolgapirim.mesup.placeholderYap
import kotlinx.android.synthetic.main.post_recycler_row.view.recyclerRowMesajText
import kotlinx.android.synthetic.main.post_recycler_row.view.recyclerRowuserName
import kotlinx.android.synthetic.main.post_recycler_row_with_images.view.*

class PostProfilAdapter(val PostList:ArrayList<Post>):RecyclerView.Adapter<PostProfilAdapter.ProfilPost>() {

    val VIEW_TYPE_WITH_IMAGES = 1
    val VIEW_TYPE_WITHOUT_IMAGES = 2
    val VIEW_TYPE_JUST_IMAGE = 3

    class ProfilPost(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun getItemViewType(position: Int): Int {

        if(PostList.get(position).postGorsel!="" && PostList.get(position).mesaj=="" ){
            return VIEW_TYPE_JUST_IMAGE
        }else if(PostList.get(position).postGorsel!=""){
            return VIEW_TYPE_WITH_IMAGES
        }
        else{
            return VIEW_TYPE_WITHOUT_IMAGES
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilPost {

        if (viewType==1){
            val view  =LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row_with_images,parent,false)
            return ProfilPost(view)
        }else if(viewType==3){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row_just_image,parent,false)
            return ProfilPost(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_recycler_row,parent,false)
            return ProfilPost(view)
        }
    }

    override fun onBindViewHolder(holder: ProfilPost, position: Int) {
        if(PostList.get(position).mesaj!=""){
            holder.itemView.recyclerRowMesajText.text = PostList[position].mesaj
        }

      holder.itemView.recyclerRowuserName.text = PostList.get(position).kullaniciAd
        if (PostList[position].postGorsel!=""){
            holder.itemView.imageView4.gorselIndir(PostList[position].postGorsel, placeholderYap(holder.itemView.context))
        }

        if(PostList[position].profilResim!=""){
            holder.itemView.recyclerRowprofilImages.gorselIndir(PostList[position].profilResim,
                placeholderYap(holder.itemView.context))
        }





    }

    override fun getItemCount(): Int {
        return PostList.size
    }
}