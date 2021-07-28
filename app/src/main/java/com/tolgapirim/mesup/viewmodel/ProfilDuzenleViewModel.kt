package com.tolgapirim.mesup.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tolgapirim.mesup.fragman.ProfilDuzenleFragmentDirections
import java.util.*
import kotlin.collections.HashMap

class ProfilDuzenleViewModel:ViewModel() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var storage:FirebaseStorage


    val profilUrlResim = MutableLiveData<String>()
    val kullaniciAdi = MutableLiveData<String>()
    val yukleniyor = MutableLiveData<Boolean>()

    fun OlanVerileriAl(){

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        database.collection("Kullanicilar").document(auth.currentUser!!.uid).get().addOnSuccessListener { document->
            kullaniciAdi.value = document["KullaniciAd"] as String
            if (document["kullaniciProfilResmi"].toString()!=""){
                profilUrlResim.value = document["kullaniciProfilResmi"] as String
            }

        }
    }

    fun VerileriGuncelle(secilenGorsel: Uri?,view:View,kullaniciAdiText:String,biyografiText:String){
        storage = FirebaseStorage.getInstance()
        yukleniyor.value = true

        val profilDuzenleHashMapKullaniciCollection= HashMap<String,Any>()
        if(secilenGorsel!=null){
            var downladUrl=""
            val storageReference = storage.reference
            val uuid = UUID.randomUUID()
            val gorselIsim = "${uuid}.jpg"
            val gorselReference = storageReference.child("ProfilImages").child(gorselIsim)

            gorselReference.putFile(secilenGorsel).addOnSuccessListener {
                gorselReference.downloadUrl.addOnSuccessListener {
                    downladUrl = it.toString()

                    profilDuzenleHashMapKullaniciCollection.put("KullaniciAd",kullaniciAdiText)



                    profilDuzenleHashMapKullaniciCollection.put("kullaniciProfilResmi",downladUrl)




                    database.collection("Kullanicilar").document(auth.currentUser!!.uid).update(profilDuzenleHashMapKullaniciCollection).addOnSuccessListener {
                        yukleniyor.value = false
                        val action = ProfilDuzenleFragmentDirections.actionProfilDuzenleFragmentToProfilFragment()
                        Navigation.findNavController(view).navigate(action)
                    }

                }
            }


        }else{
            profilDuzenleHashMapKullaniciCollection.put("KullaniciAd",kullaniciAdiText)

            database.collection("Kullanicilar").document(auth.currentUser!!.uid).update(profilDuzenleHashMapKullaniciCollection).addOnSuccessListener {
                yukleniyor.value = false
                val action = ProfilDuzenleFragmentDirections.actionProfilDuzenleFragmentToProfilFragment()
                Navigation.findNavController(view).navigate(action)
            }


        }


        database.collection("Post")
    }



}