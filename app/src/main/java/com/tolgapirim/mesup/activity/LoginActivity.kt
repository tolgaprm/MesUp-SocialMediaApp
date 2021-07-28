package com.tolgapirim.mesup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.diger.FirebaseAuthTurkce
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    val firebaseTurkce = FirebaseAuthTurkce()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()


        database = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()

        btnGirisYap.setOnClickListener {

            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            if(email==""|| password==""){
                Toast.makeText(this,"Lütfen Alanları Doldurunuz", Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                      AnaSayfayaGit()
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,firebaseTurkce.TurkceMesaj(it.localizedMessage), Toast.LENGTH_LONG).show()
                }
            }
        }

        btnKayitOlSayfaGit.setOnClickListener {
            val intent = Intent(this, KayitOlActivity::class.java)
            startActivity(intent)
        }



    }


    fun AnaSayfayaGit(){
        val intent = Intent(this, AnasayfaActivity::class.java)
        this.startActivity(intent)
    }



}