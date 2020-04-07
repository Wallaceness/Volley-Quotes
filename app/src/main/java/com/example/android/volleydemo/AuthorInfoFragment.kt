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

    var binding:FragmentAuthorInfoBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_author_info, container, false)
        if (savedInstanceState!=null){
            binding?.quote = savedInstanceState.getParcelable("quote_item")
        }
        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("quote_item", binding?.quote)
        super.onSaveInstanceState(outState)
    }

}
