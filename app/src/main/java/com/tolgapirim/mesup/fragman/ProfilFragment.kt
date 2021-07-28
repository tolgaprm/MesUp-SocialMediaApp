package com.tolgapirim.mesup.fragman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolgapirim.mesup.Adapter.PostProfilAdapter
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.gorselIndir
import com.tolgapirim.mesup.placeholderYap
import com.tolgapirim.mesup.viewmodel.ProfilViewModel
import kotlinx.android.synthetic.main.fragment_profil.*


class ProfilFragment : Fragment() {


    private lateinit var recyclerViewAdapter:PostProfilAdapter
    private lateinit var viewModel:ProfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(ProfilViewModel::class.java)

        viewModel.verileriAl()
        ObserveLiveData()


        profilDuzenleBtn.setOnClickListener {
            val action = ProfilFragmentDirections.actionProfilFragmentToProfilDuzenleFragment()
            Navigation.findNavController(it).navigate(action)
        }



    }



    fun ObserveLiveData(){
        viewModel.kullaniciAd.observe(viewLifecycleOwner,{
            profilKullaniciAd.text = it
        })

        viewModel.profilImagesUrl.observe(viewLifecycleOwner,{ url->
            if (url!=""){
                profilImages.gorselIndir(url, placeholderYap(this.requireContext()))
            }
        })

        viewModel.postList.observe(viewLifecycleOwner,{

            val layoutManager = LinearLayoutManager(this.context)
            recyclerViewProfil.layoutManager = layoutManager


            recyclerViewAdapter = PostProfilAdapter(it)
            recyclerViewProfil.adapter = recyclerViewAdapter
        })
    }



}