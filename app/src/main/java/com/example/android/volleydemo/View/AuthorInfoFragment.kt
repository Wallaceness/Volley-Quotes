package com.example.android.volleydemo.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.example.android.volleydemo.R
import com.example.android.volleydemo.databinding.FragmentAuthorInfoBinding

/**
 * A simple [Fragment] subclass.
 */
class AuthorInfoFragment : Fragment() {

    lateinit var binding:FragmentAuthorInfoBinding
    lateinit var collapseExpandButton:ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_author_info, container, false)
        if (savedInstanceState!=null){
            binding.quote = savedInstanceState.getParcelable("quote_item")
        }
        binding.expanded = true

        collapseExpandButton = binding.root.findViewById(R.id.collapseButton)
        collapseExpandButton.setOnClickListener({
            binding.expanded = if (binding.expanded==true) false else true
        })
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("quote_item", binding.quote)
        super.onSaveInstanceState(outState)
    }

}
