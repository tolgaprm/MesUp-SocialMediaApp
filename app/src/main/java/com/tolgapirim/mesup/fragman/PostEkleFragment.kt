package com.tolgapirim.mesup.fragman

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.gorselIndir
import com.tolgapirim.mesup.model.Post
import com.tolgapirim.mesup.placeholderYap
import com.tolgapirim.mesup.viewmodel.PostEkleViewModel
import kotlinx.android.synthetic.main.fragment_post_ekle.*
import java.util.*


class PostEkleFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher:ActivityResultLauncher<String>
    private var secilenGorsel:Uri?= null
    private var secilenBitmap:Bitmap?=null
    private lateinit var viewModel:PostEkleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_ekle, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         viewModel = ViewModelProviders.of(this).get(PostEkleViewModel::class.java)


        viewModel.KullaniciVerileriniAl()
        registerLauncher()


        postEkleBtnGaleri.setOnClickListener {
            resimSec(it)
        }


        postEklebtnPaylas.setOnClickListener {

            val timestamp = Timestamp.now()
            val post = Post(viewModel.kullaniciAd.value.toString(),viewModel.kullaniciUidVm.value.toString(),postEkleMesaj.text.toString(),"",timestamp,viewModel.profilImage.value.toString())
            viewModel.PostuKaydet(post,it,this.requireContext(),secilenGorsel)

        }


        ObserveLiveData()
    }


   fun resimSec(view:View){
        activity?.let {
            if(ContextCompat.checkSelfPermission(it,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(it,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //rational yeni izin istemekte kullanılır.
                    Snackbar.make(view,"Galeri için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver",View.OnClickListener {
                        // request permission
                        permissionsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                }else{
                    // request permission
                    permissionsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }


            }else{
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)

            }
        }

    }



    fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode == Activity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult!=null){
                    secilenGorsel= intentFromResult.data
                    if (secilenGorsel!=null){
                        activity?.let {
                        if (Build.VERSION.SDK_INT>=28){

                                val source = ImageDecoder.createSource(it.contentResolver,secilenGorsel!!)
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                imagePost.visibility = View.VISIBLE
                                imagePost.setImageBitmap(secilenBitmap)


                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel)
                            imagePost.visibility = View.VISIBLE
                            imagePost.setImageBitmap(secilenBitmap)
                            }
                        }
                    }
                }
            }

        }


        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }else{
                Toast.makeText(context,"İzine İhtiyaç Var",Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun ObserveLiveData(){
        viewModel.kullaniciAd.observe(viewLifecycleOwner,{
            postEkleKullaniciAdi.text = it
        })

        viewModel.profilImage.observe(viewLifecycleOwner,{
            postEkleProfilImage.gorselIndir(it, placeholderYap(this.requireContext()))
        })

        viewModel.progressBar.observe(viewLifecycleOwner,{
            if (it){
                postEkleprogressBar.visibility=View.VISIBLE
            }else{
                postEkleprogressBar.visibility = View.GONE
            }
        })


    }

}