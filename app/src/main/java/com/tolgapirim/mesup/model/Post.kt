package com.tolgapirim.mesup.model

import android.net.Uri

class Post(

    var kullaniciAd:String,
    val uid:String,
    val mesaj:String,
    val postGorsel: String,
    val tarih: com.google.firebase.Timestamp,
    var profilResim:String,

    ) {

    fun KullaniciAdGuncelle(yenikullaniciAd: String):String{
        kullaniciAd = yenikullaniciAd

        return kullaniciAd
    }

    fun profilResimGuncelle(yeniProfilUrl:String):String{
        profilResim = yeniProfilUrl
        return profilResim
    }

    var postGorselURi:Uri? =null

}