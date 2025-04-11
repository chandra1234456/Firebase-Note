package com.example.practice.notesfirebase.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.notesfirebase.MainActivity
import com.example.practice.notesfirebase.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var searchBinding : FragmentSearchBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        searchBinding = FragmentSearchBinding.inflate(layoutInflater)
        return searchBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }

}