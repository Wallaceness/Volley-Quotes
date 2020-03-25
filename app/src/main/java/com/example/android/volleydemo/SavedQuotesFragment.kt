package com.example.android.volleydemo

import android.app.AlertDialog
import android.content.DialogInterface
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
import kotlin.jvm.internal.CallableReference

/**
 * A simple [Fragment] subclass.
 */
class SavedQuotesFragment : Fragment() {
    var savedQuotes=arrayListOf<Quote>()
    lateinit var qvm:QuoteViewModel;
    lateinit var deleteDialog:DeleteAlert
    lateinit var selectedQuote:Quote
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            // Inflate the layout for this fragment
            val rootView:View= inflater.inflate(R.layout.fragment_saved_quotes, container, false)
            val recycleView = rootView.findViewById<RecyclerView>(R.id.savedQuotesRecyclerview)
            qvm = QuoteViewModel(requireActivity().application)
            val adapter= SavedQuotesAdapter(savedQuotes, this)
            val layoutManager = LinearLayoutManager(requireContext())
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter
                qvm.getSavedQuotes().observe(viewLifecycleOwner, Observer { response->
                savedQuotes=response as ArrayList<Quote>
                adapter.updateQuotes(savedQuotes)
            })
            return rootView
    }

    fun deleteQuote(){
        qvm.deleteQuote(selectedQuote)
    }

    fun launchDialog(quote: Quote){
        selectedQuote = quote
        deleteDialog = DeleteAlert(this)
        deleteDialog.show(childFragmentManager, "DeleteAlertDialog")
    }

}
