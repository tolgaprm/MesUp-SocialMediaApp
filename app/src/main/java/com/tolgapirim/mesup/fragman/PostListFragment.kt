package com.tolgapirim.mesup.fragman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolgapirim.mesup.Adapter.PostListAdapter
import com.tolgapirim.mesup.Adapter.PostProfilAdapter
import com.tolgapirim.mesup.R
import com.tolgapirim.mesup.viewmodel.PostListViewModel
import kotlinx.android.synthetic.main.fragment_post_list.*


class PostListFragment : Fragment() {

    private lateinit var viewModel:PostListViewModel
    private lateinit var recyclerViewAdapter:PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PostListViewModel::class.java)
        viewModel.PostlariAl()

        viewModel.postList.observe(viewLifecycleOwner,{
          val layoutManager = LinearLayoutManager(this.requireContext())
          recyclerView.layoutManager = layoutManager

        recyclerViewAdapter = PostListAdapter(it)
        recyclerView.adapter = recyclerViewAdapter


        })

    }

}