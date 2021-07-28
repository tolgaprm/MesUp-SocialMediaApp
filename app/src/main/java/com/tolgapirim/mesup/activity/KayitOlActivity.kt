package com.tolgapirim.mesup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.diger.FirebaseAuthTurkce
import kotlinx.android.synthetic.main.activity_kayit_ol.*

class KayitOlActivity : AppCompatActivity() {
    private lateinit var  auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    val firebaseTurkce = FirebaseAuthTurkce()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        btnToLoginActiviy.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKayitOL.setOnClickListener {
            val kullaniciAdi = kullaniciAdiText.text.toString()
            val email = emailText.text.toString()
            val sifre =passwordText.text.toString()
            val tSifre = passwordTextAgain.text.toString()
            if(kullaniciAdi=="" || email=="" || sifre=="" || tSifre==""){
                Toast.makeText(this,"Lütfen gerekli yerleri doldurunuz", Toast.LENGTH_LONG).show()
            }else{
                if (sifre==tSifre){
                    auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener {
                        if(it.isSuccessful){
                            val kullaniciID = auth.currentUser?.uid

                            val kullaniciHashMap = hashMapOf<String,String>()

                            kullaniciHashMap.put("KullaniciEmail",email)
                            kullaniciHashMap.put("KullaniciAd",kullaniciAdi)
                            kullaniciHashMap.put("kullaniciProfilResmi","")
                            kullaniciHashMap.put("kullaniciUid",auth.currentUser!!.uid)
                            kullaniciHashMap.put("biyografi","")


                            if(kullaniciID!=null){
                                database.collection("Kullanicilar").document("${kullaniciID}").set(kullaniciHashMap).addOnCompleteListener {
                                    val intent = Intent(this, AnasayfaActivity::class.java)
                                    this.startActivity(intent)
                                    finish()

                                }.addOnFailureListener {
                                    Toast.makeText(this,firebaseTurkce.TurkceMesaj(it.localizedMessage), Toast.LENGTH_LONG).show()
                                }

                            }

                        }
                    }.addOnFailureListener {
                        Toast.makeText(this,firebaseTurkce.TurkceMesaj(it.localizedMessage), Toast.LENGTH_LONG).show()

                    }

                }else {
                    Toast.makeText(this,"Şifreler Uyuşmuyor", Toast.LENGTH_LONG).show()


                }

        }

    }}
}
