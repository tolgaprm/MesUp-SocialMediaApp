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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.gorselIndir
import com.tolgapirim.mesup.placeholderYap
import com.tolgapirim.mesup.viewmodel.ProfilDuzenleViewModel
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.android.synthetic.main.fragment_profil_duzenle.*



class ProfilDuzenleFragment : Fragment() {

    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap?=null
    private lateinit var viewModel: ProfilDuzenleViewModel

    private lateinit var requestPermissionLaunch:ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profil_duzenle, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfilDuzenleViewModel::class.java)

        viewModel.OlanVerileriAl()
        ObserveLiveData()
        registerLauncher()

        guncelleBtn.setOnClickListener {
            val kullaniciText = profilDuzenleKullaniciAdi.text.toString()
            val biyografi = profilDuzenleBiyografi.text.toString()
            viewModel.VerileriGuncelle(secilenGorsel,it,kullaniciText,biyografi)
        }

        iptalBtn.setOnClickListener {
            iptal(it)
        }

        profilResmiDegistir.setOnClickListener {
            profilResmiKaydet(it)
        }
    }

    // Profil Resmi VArsa Profil Resmini Koy ImageView

    fun profilResmiKaydet(view: View){
        if(ContextCompat.checkSelfPermission(this.requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeri için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                    requestPermissionLaunch.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                requestPermissionLaunch.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }else{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
    }


    fun iptal(view: View){
        val action =
           ProfilDuzenleFragmentDirections.actionProfilDuzenleFragmentToProfilFragment()
        Navigation.findNavController(view).navigate(action)
    }


    fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

            if (result.resultCode == Activity.RESULT_OK){

                val intentFormIntent = result.data
                if (intentFormIntent!=null){
                    secilenGorsel = intentFormIntent.data
                    if (secilenGorsel!=null){

                        activity?.let {
                            if (Build.VERSION.SDK_INT>=28){
                                val source  = ImageDecoder.createSource(it.contentResolver,secilenGorsel!!)
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                profilDuzenleProfil_image.setImageBitmap(secilenBitmap)
                            }else{
                                secilenBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,secilenGorsel)
                                profilDuzenleProfil_image.setImageBitmap(secilenBitmap)
                            }

                        }

                    }
                }
            }

        }

        requestPermissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
        }
    }

    fun ObserveLiveData(){
        viewModel.kullaniciAdi.observe(viewLifecycleOwner,{
            profilDuzenleKullaniciAdi.setText(it)
        })

        viewModel.yukleniyor.observe(viewLifecycleOwner,{
            if (it){
                profilDuzenleProgressBar.visibility = View.VISIBLE
            }else{
                profilDuzenleProgressBar.visibility = View.GONE
            }
        })
        viewModel.profilUrlResim.observe(viewLifecycleOwner,{
            profilDuzenleProfil_image.gorselIndir(it, placeholderYap(this.requireContext()))
        })
    }
}