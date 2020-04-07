package com.example.android.volleydemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.android.volley.VolleyError
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class MainFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    var quoteVM: QuoteViewModel?=null
    var quote: Quote?=null
    lateinit var tabs:TabLayout
    var currentTab:String = "random"
    lateinit var singleQuoteFragment:SingleQuoteFragment
    lateinit var multiQuoteFragment:FetchedQuotesFragment
    lateinit var authorFragment:AuthorInfoFragment
    var viewType = "single"
    var searchValue:String?=null
    lateinit var manager:FragmentManager
    lateinit var authorContainer:View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        QuoteViewModel.VolleyQueue.init(requireActivity().applicationContext)
        val rootView:View = inflater.inflate(R.layout.fragment_main, container, false)
        quoteVM = QuoteViewModel(requireActivity().application)
        tabs = rootView.findViewById(R.id.tabLayout)
        manager = childFragmentManager
        authorContainer = rootView.findViewById(R.id.authorInfo)
        authorFragment = AuthorInfoFragment()
        //reset state after orientation change
        if (savedInstanceState!=null){
            this.quote = savedInstanceState.getParcelable("single_quote")
            currentTab = savedInstanceState.getString("current_tab")!!
            viewType = savedInstanceState.getString("view_type") as String
            searchValue = savedInstanceState.getString("search_term")
            if (viewType == "single"){
                singleQuoteFragment = SingleQuoteFragment(this.quote)
                multiQuoteFragment = FetchedQuotesFragment(currentTab)
            }
            else if (viewType == "multi"){
                multiQuoteFragment = manager.findFragmentById(R.id.quoteBody) as FetchedQuotesFragment
                singleQuoteFragment = SingleQuoteFragment()
            }
            if (currentTab == "author"){
                authorFragment=manager.findFragmentById(R.id.authorInfo) as AuthorInfoFragment
                if (this.quote!=null){
                    authorContainer.visibility = View.VISIBLE
                }
            }
        }
        else{
            singleQuoteFragment = SingleQuoteFragment()
            multiQuoteFragment = FetchedQuotesFragment(currentTab)
            manager.beginTransaction().add(R.id.controlPanel, RandomFragment()).commit()
        }

        when(currentTab){
                "random"-> tabs.getTabAt(0)?.select()
                "keyword"->tabs.getTabAt(1)?.select()
                "author"->tabs.getTabAt(2)?.select()
        }

        if (viewType == "single"){
            singleQuoteFragment.setBinding(null)
            manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
        }
        else if (viewType == "multi"){
            manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
        }

        tabs.addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                println("Tab reselected")
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                println("Tab unselected")
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                manager.beginTransaction().replace(R.id.controlPanel, when(p0?.text){
                    "Random"-> {
                        currentTab = "random"
                        manager.beginTransaction().remove(authorFragment).commit()
                        authorContainer.visibility=View.GONE
                        multiQuoteFragment.resetType("random")
                        RandomFragment()
                    }
                    "By Keyword" ->{
                        currentTab = "keyword"
                        manager.beginTransaction().remove(authorFragment).commit()
                        authorContainer.visibility=View.GONE
                        multiQuoteFragment.resetType("keyword")
                        SearchFragment()
                    }
                    "By Author" ->{
                        currentTab = "author"
                        manager.beginTransaction().replace(R.id.authorInfo, authorFragment).commit()
                        multiQuoteFragment.resetType("author")
                        SearchFragment()
                    }
                    else -> Fragment()
                }).commit()
                quote = null
                if (viewType == "single"){
                    singleQuoteFragment.setBinding(null)
                    manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
                }
                else if (viewType == "multi"){
                    multiQuoteFragment = FetchedQuotesFragment(currentTab)
                    manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
                }
                searchValue = null

            }

        })
        quoteVM?.getQuote()?.observe(viewLifecycleOwner, Observer<JSONObject> {response->
            val quote = Quote(
                response.optString("message", ""),
                response.optString("author", ""),
                response.optString("keywords", ""),
                response.optString("profession", ""),
                response.optString("nationality", ""),
                response.optString("authorBirth", ""),
                response.optString("authorDeath", "")
            )
            this.quote = quote
            if (currentTab == "author"){
                showAuthorInfo(quote)
            }
            singleQuoteFragment.setBinding(quote)
        })
        quoteVM?.getError()?.observe(viewLifecycleOwner, Observer<VolleyError> { e ->
            val responseBody = String(e.networkResponse.data)
            Toast.makeText(requireContext(), responseBody, Toast.LENGTH_LONG).show()
        })

        if (savedInstanceState==null){
            quoteVM!!.fetchRandom()
        }
        setHasOptionsMenu(true)
        return rootView
    }

    fun newSearchValue(term:String){
        searchValue = term
        if (viewType == "multi"){
            multiQuoteFragment.startFetching()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==R.id.singleQuote){
            if (viewType !="single") {
                manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
                viewType="single"
            }
        }
        else if (id==R.id.multiQuote){
            if (viewType!="multi"){
                manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
                viewType="multi"
                this.quote = null
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showAuthorInfo(quote:Quote?){
        authorContainer.visibility = View.VISIBLE
        authorFragment.binding?.quote =quote
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("single_quote", this.quote)
        outState.putString("view_type", this.viewType)
        outState.putString("current_tab", this.currentTab)
        outState.putString("search_term", this.searchValue)
        super.onSaveInstanceState(outState)
    }
}
