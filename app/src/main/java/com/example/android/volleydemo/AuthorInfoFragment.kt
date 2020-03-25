package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.volleydemo.databinding.FragmentAuthorInfoBinding

/**
 * A simple [Fragment] subclass.
 */
class AuthorInfoFragment : Fragment() {

    lateinit var binding:FragmentAuthorInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_author_info, container, false)
        return binding.root
    }

}
