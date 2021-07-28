package com.tolgapirim.mesup.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tolgapirim.mesup.fragman.PostEkleFragmentDirections
import com.tolgapirim.mesup.model.Post
import java.util.*

class PostEkleViewModel:ViewModel() {

    val profilImage = MutableLiveData<String>()
    val kullaniciAd = MutableLiveData<String>()
    val progressBar = MutableLiveData<Boolean>()
    val kullaniciUidVm= MutableLiveData<String>()


    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var storage:FirebaseStorage


    fun KullaniciVerileriniAl(){
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val guncelKullanici = auth.currentUser
        var kullaniciAdDatabase=""

        database.collection("Kullanicilar").document(guncelKullanici!!.uid).get().addOnSuccessListener { document->
            document?.let {
                 kullaniciAdDatabase = it["KullaniciAd"].toString()
                kullaniciAd.value = kullaniciAdDatabase

                val kullaniciProfilImageDatabase = it["kullaniciProfilResmi"].toString()
                profilImage.value = kullaniciProfilImageDatabase

                val kullaniciUid = it["kullaniciUid"].toString()
                kullaniciUidVm.value = kullaniciUid
            }

        }
    }

    fun PostuKaydet(post:Post,view: View,context: Context,postGorselUri: Uri?){
        storage = FirebaseStorage.getInstance()

        val postHashMAp = hashMapOf<String,Any>()

        if(post.mesaj=="" && postGorselUri==null){
            Toast.makeText(context,"Paylaşım yapabilmek için bir mesaj veya bir görsel yüklemelisiniz",Toast.LENGTH_LONG).show()


        } else if (post.mesaj!="" && postGorselUri==null){
            progressBar.value = true

            postHashMAp.put("kullaniciUid",post.uid)
            postHashMAp.put("tarih",post.tarih)
            postHashMAp.put("mesaj",post.mesaj)

            postHashMAp.put("kullaniciAd",kullaniciAd.value.toString())
            postHashMAp.put("kullaniciProfilImage",profilImage.value.toString())
            postHashMAp.put("postGorsel","")
            database.collection("Post").add(postHashMAp).addOnCompleteListener {

                val action = PostEkleFragmentDirections.actionPostEkleFragmentToPostListFragment()
                Navigation.findNavController(view).navigate(action)
            }

        } else if (postGorselUri!=null){
            progressBar.value = true
            val uuid = UUID.randomUUID()
            val gorselAd = "${uuid}.jpg"
            val storageReference = storage.reference

            val gorselReference  = storageReference.child("PostGorsel").child(gorselAd)

            gorselReference.putFile(postGorselUri).addOnSuccessListener {
                gorselReference.downloadUrl.addOnSuccessListener {

                    val gorselUrl = it.toString()

                    postHashMAp.put("kullaniciUid",post.uid)
                    postHashMAp.put("kullaniciAd",kullaniciAd.value.toString())
                    postHashMAp.put("kullaniciProfilImage",profilImage.value.toString())
                    postHashMAp.put("postGorsel",gorselUrl)
                    postHashMAp.put("tarih",post.tarih)
                    postHashMAp.put("mesaj","")

                    if (post.mesaj!=""){
                        postHashMAp.put("mesaj",post.mesaj)
                   }

                    database.collection("Post").add(postHashMAp).addOnSuccessListener {
                        val action = PostEkleFragmentDirections.actionPostEkleFragmentToPostListFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
        }
        progressBar.value = false
    }

}