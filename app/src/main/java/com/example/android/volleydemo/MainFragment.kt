package com.example.android.volleydemo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
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
    var tabs:TabLayout?=null
    var currentTab:String = "random"
    lateinit var singleQuoteFragment:SingleQuoteFragment
    lateinit var multiQuoteFragment:FetchedQuotesFragment
    lateinit var authorFragment:AuthorInfoFragment
    var viewType = "single"
    var searchValue:String?=null
    lateinit var manager:FragmentManager
    lateinit var authorContainer:View
    lateinit var searcher: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        QuoteViewModel.VolleyQueue.init(requireActivity().applicationContext)
        val rootView:View = inflater.inflate(R.layout.fragment_main, container, false)
        quoteVM = QuoteViewModel(requireActivity().application)
        //initialize tabs only if in portrait view
        if (resources.configuration.orientation ==Configuration.ORIENTATION_PORTRAIT){
            tabs = rootView.findViewById(R.id.tabLayout)

        }
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
        //settings if this is the first time loading fragment
        else{
            val intent: Intent? = activity?.getIntent()
            val intentQuote:Quote? = intent?.getParcelableExtra("notification_quote")
            if (intentQuote!=null ){
                this.quote = intentQuote
            }
            singleQuoteFragment = SingleQuoteFragment(this.quote)
            multiQuoteFragment = FetchedQuotesFragment(currentTab)
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if (currentTab == "random") {
                manager.beginTransaction().replace(R.id.controlPanel, RandomFragment()).commit()
            }
            else{
                val searchFragment = SearchFragment(searchValue?:"")
                manager.beginTransaction().replace(R.id.controlPanel, searchFragment).commit()
            }
        }

        when(currentTab){
                "random"-> tabs?.getTabAt(0)?.select()
                "keyword"->tabs?.getTabAt(1)?.select()
                "author"->tabs?.getTabAt(2)?.select()
        }

        if (viewType == "single"){
            manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
        }
        else if (viewType == "multi"){
            manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
        }

        tabs?.addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
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

        if (savedInstanceState==null && this.quote==null){
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
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            inflater.inflate(R.menu.action_menu, menu)
        }
        else{
            var initial:Boolean = true
            inflater.inflate(R.menu.action_menu_landscape, menu)
//            set up spinner
            val spinner = menu.findItem(R.id.toolbarSpinner).actionView as Spinner
            searcher = menu.findItem(R.id.searchBar).actionView as EditText
            searcher.width=300
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(when(currentTab){
                "random"->0
                "keyword"->1
                "author"->2
                else-> 0
            })
            spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!initial){
                    when(parent?.getItemAtPosition(position) as String){
                        "Random"-> {
                            searcher.visibility = View.INVISIBLE
                            currentTab = "random"
                            manager.beginTransaction().remove(authorFragment).commit()
                            authorContainer.visibility=View.GONE
                            multiQuoteFragment.resetType("random")
                            RandomFragment()
                        }
                        "By Keyword" ->{
                            searcher.visibility = View.VISIBLE
                            currentTab = "keyword"
                            manager.beginTransaction().remove(authorFragment).commit()
                            authorContainer.visibility=View.GONE
                            multiQuoteFragment.resetType("keyword")
                            SearchFragment()
                        }
                        "By Author" ->{
                            searcher.visibility = View.VISIBLE
                            currentTab = "author"
                            manager.beginTransaction().replace(R.id.authorInfo, authorFragment).commit()
                            multiQuoteFragment.resetType("author")
                            SearchFragment()
                        }
                    }
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

                    searcher.text = null

            }
                    initial = false
                } }
            //set values on spinner and setter from previous orientation

            //initialize searchbar
            if (searchValue!=null) searcher.setText(searchValue)

            searcher.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    searchValue = s?.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.singleQuote->{
                if (viewType !="single") {
                    manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
                    viewType="single"
                }
            }
            R.id.multiQuote->{
                if (viewType!="multi"){
                    manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
                    viewType="multi"
                    this.quote = null
                }
                else{
                    multiQuoteFragment.toggleView("linear")
                }
            }
            R.id.gridQuote->{
                if (viewType!="multi"){
                    multiQuoteFragment = FetchedQuotesFragment(currentTab, true)
                    manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
                    viewType=="multi"
                    this.quote=null
                }
                else{
                    multiQuoteFragment.toggleView("grid")
                }
            }
            R.id.toolbarButton ->{
                val term = searcher.text.toString()
                when(currentTab){
                    "random"->{
                        quoteVM?.fetchRandom()
                    }
                    "author"->{
                        if (term!=""){
                            searchValue = term
                            quoteVM?.fetchByAuthor(searchValue!!)
                        }
                    }
                    "keyword"->{
                        if (term!=""){
                            searchValue = term
                            quoteVM?.fetchByKeyword(searchValue!!)
                        }
                    }
                }
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
