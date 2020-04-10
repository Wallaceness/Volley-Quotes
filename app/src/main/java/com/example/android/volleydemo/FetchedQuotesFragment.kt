package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.View.MainActivity
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class FetchedQuotesFragment(): Fragment() {


    lateinit var vm:QuoteViewModel
    lateinit var type:String
    lateinit var parent:MainFragment
    lateinit var emptyView:TextView
    lateinit var adapter: FetchedQuotesAdapter
    var recycler:RecyclerView?=null
    var quotes = ArrayList<Quote>()
    var grid=false

    constructor(type:String, grid:Boolean=false) : this() {
        this.type=type
        this.grid = grid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = QuoteViewModel(requireActivity().application)
        val rootView:View = inflater.inflate(R.layout.fragment_fetched_quote, container, false)
        recycler= rootView.findViewById(R.id.fetchedQuoteRecycler)
        parent = parentFragment as MainFragment
        if (savedInstanceState!=null){
            quotes = savedInstanceState.getParcelableArrayList<Quote>("quote_list") as ArrayList<Quote>
            type = savedInstanceState.getString("quote_type")!!
            if (type=="author" && quotes.size>0){
                parent.authorContainer.visibility = View.VISIBLE
            }
        }
        adapter= FetchedQuotesAdapter(quotes, this)
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
                parent.showAuthorInfo(quote)
            }
            adapter.addQuote(quote)
        })
        if (grid){
            recycler?.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        else{
            recycler?.layoutManager = LinearLayoutManager(requireContext())
        }
        recycler?.adapter = adapter
        startFetching()
        recycler?.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    (activity as MainActivity).toggleBottomNavigation(true)
                }
                else if (dy<0){
                    (activity as MainActivity).toggleBottomNavigation(false)
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        return rootView
    }

    fun toggleView(view:String){
        if (view == "grid"){
            recycler?.layoutManager = GridLayoutManager(requireContext(),2)
        }
        else if (view == "linear"){
            recycler?.layoutManager = LinearLayoutManager(requireContext())
        }
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

    fun saveQuote(quote:Quote){
        vm.saveQuote(quote)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("quote_list", quotes)
        outState.putString("quote_type", type)
        super.onSaveInstanceState(outState)
    }

}
