package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.ViewModel.QuoteViewModel

/**
 * A simple [Fragment] subclass.
 */
class SavedQuotesFragment : Fragment() {
    var savedQuotes=arrayListOf<Quote>()
    lateinit var qvm:QuoteViewModel;
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            // Inflate the layout for this fragment
            val rootView:View= inflater.inflate(R.layout.fragment_saved_quotes, container, false)
            val recycleView = rootView.findViewById<RecyclerView>(R.id.savedQuotesRecyclerview)
            qvm = QuoteViewModel(requireActivity().application)
            val adapter= SavedQuotesAdapter(savedQuotes, qvm)
            val layoutManager = LinearLayoutManager(requireContext())
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter
                qvm.getSavedQuotes().observe(viewLifecycleOwner, Observer { response->
                savedQuotes=response as ArrayList<Quote>
                adapter.updateQuotes(savedQuotes)
            })
            return rootView
    }

}
