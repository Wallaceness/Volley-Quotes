package com.example.android.volleydemo

import android.app.Application
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class FetchedQuotesFragment constructor(type:String): Fragment() {
    lateinit var vm:QuoteViewModel
    var type=type
    lateinit var parent:MainFragment
    lateinit var emptyView:TextView
    var quotes = ArrayList<Quote>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = QuoteViewModel(requireActivity().application)
        val rootView:View = inflater.inflate(R.layout.fragment_fetched_quote, container, false)
        val recycler:RecyclerView = rootView.findViewById(R.id.fetchedQuoteRecycler)
        if (savedInstanceState!=null){
            quotes = savedInstanceState.getParcelableArrayList<Quote>("quote_list") as ArrayList<Quote>
        }
        val adapter= FetchedQuotesAdapter(quotes)
        parent = parentFragment as MainFragment
        emptyView = rootView.findViewById(R.id.view_empty)
        adapter.setOnBottomReachedListener(object: onBottomReachedListener{
            override fun onBottomReached(position: Int) {
                if (type=="random"){
                    vm.fetchRandom()
                }
                else if (type=="author" && parent.searchValue!=null){
                    vm.fetchByAuthor(parent.searchValue!!)
                }
                else if (type=="keyword" && parent.searchValue!=null){
                    vm.fetchByKeyword(parent.searchValue!!)
                }
            }
        })

        vm.getQuote().observe(viewLifecycleOwner, Observer<JSONObject> { response ->
            emptyView.visibility = View.GONE
            val quote = Quote(
                response.optString("message", ""),
                response.optString("author", ""),
                response.optString("keywords", ""),
                response.optString("profession", ""),
                response.optString("nationality", ""),
                response.optString("authorBirth", ""),
                response.optString("authorDeath", "")
            )
            if (type=="author"){
                parent.authorFragment.binding.quote = quote
            }
            adapter.addQuote(quote)
        })
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        startFetching()
        return rootView
    }

    fun resetType(type:String){
        this.type=type
    }

    fun startFetching(){
        if (type=="random"){
            vm.fetchRandom()
        }
        else if (type=="author" && parent.searchValue!=null){
            vm.fetchByAuthor(parent.searchValue!!)
        }
        else if (type=="keyword" && parent.searchValue!=null){
            vm.fetchByKeyword(parent.searchValue!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArray("quote_list", quotes.toTypedArray())
        super.onSaveInstanceState(outState)
    }

}
