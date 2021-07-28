package com.tolgapirim.mesup.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tolgapirim.mesup.model.Post

class PostListViewModel:ViewModel() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore


    val postListesi = ArrayList<Post>()
    val postList = MutableLiveData<ArrayList<Post>>()


    fun PostlariAl(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()




        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            value?.let {
                if (!value.isEmpty){
                    postListesi.clear()
                    for (document in value){
                        val kullaniciAd = document["kullaniciAd"] as String
                        val kullanUid = document["kullaniciUid"] as String
                        val profilImage = document["kullaniciProfilImage"] as String
                        val postGorsel = document["postGorsel"] as String
                        val tarih = document["tarih"] as Timestamp
                        val mesaj = document["mesaj"] as String

                        val post = Post(kullaniciAd,kullanUid,mesaj,postGorsel,tarih,profilImage)
                        postListesi.add(post)
                    }
                    postList.value =postListesi
                }
            }
        }


    }










}