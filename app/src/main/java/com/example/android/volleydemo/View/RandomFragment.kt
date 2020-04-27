package com.example.android.volleydemo.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.android.volleydemo.R

/**
 * A simple [Fragment] subclass.
 */
class RandomFragment : Fragment() {
    lateinit var randomBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView:View =inflater.inflate(R.layout.fragment_random, container, false)
        randomBtn = rootView.findViewById(R.id.randomBtn)
        val mvModel = (parentFragment as MainFragment).quoteVM

        randomBtn.setOnClickListener({v->
            mvModel?.fetchRandom()
        })
        return rootView
    }

}
