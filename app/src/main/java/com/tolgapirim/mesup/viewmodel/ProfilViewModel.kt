package com.tolgapirim.mesup.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tolgapirim.mesup.model.Post

class ProfilViewModel:ViewModel() {


    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore


    val postListesi =ArrayList<Post>()
    val profilImagesUrl = MutableLiveData<String>()
    val kullaniciAd = MutableLiveData<String>()
    val postList = MutableLiveData<ArrayList<Post>>()


    fun verileriAl(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        database.collection("Kullanicilar").document(auth.currentUser!!.uid).addSnapshotListener { value, error ->

            value?.let { document->
                kullaniciAd.value = document["KullaniciAd"].toString()
                profilImagesUrl.value = document["kullaniciProfilResmi"].toString()
            }

        }


        database.collection("Post").orderBy("tarih",Query.Direction.ASCENDING).whereEqualTo("kullaniciUid",auth.currentUser!!.uid)
            .addSnapshotListener { value,error ->

            if (value!=null) {
                if (!value.isEmpty) {


                    var kullaniciUid=""
                    var mesaj=""
                    var postGorsel=""
                    var tarih:Timestamp

                    postListesi.clear()
                    for (document in value){

                        kullaniciUid = document.data["kullaniciUid"] as String
                        mesaj = document.data["mesaj"] as String
                        postGorsel = document.data["postGorsel"] as String
                        tarih = document.data["tarih"] as Timestamp

                        val post = Post(kullaniciAd.value.toString(),kullaniciUid,mesaj,postGorsel,tarih,profilImagesUrl.value.toString())
                        postListesi.add(post)

                    }

                    postList.value = postListesi

                }

            }





        }

        }

    }




