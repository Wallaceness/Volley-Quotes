package com.example.android.volleydemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.android.volley.VolleyError
import com.example.android.volleydemo.View.MainActivity
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import java.util.zip.Inflater

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
    lateinit var singleQuoteFragment:SingleQuoteFragment;
    lateinit var multiQuoteFragment:FetchedQuotesFragment;
    lateinit var toggleButton: Button
    var viewType = "single"
    var searchValue:String?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        QuoteViewModel.VolleyQueue.init(requireActivity()!!.applicationContext)
        val rootView:View = inflater.inflate(R.layout.fragment_main, container, false)
        quoteVM = QuoteViewModel(requireActivity().application)
        tabs = rootView!!.findViewById(R.id.tabLayout)
        val manager = childFragmentManager
        manager.beginTransaction().add(R.id.controlPanel, RandomFragment()).commit()
        singleQuoteFragment = SingleQuoteFragment()
        multiQuoteFragment = FetchedQuotesFragment(currentTab)
        manager.beginTransaction().add(R.id.quoteBody, singleQuoteFragment).commit()

        val navButton:Button =rootView.findViewById(R.id.navBtn)
        navButton.setOnClickListener { v->
            (activity as MainActivity).navigateTo(R.id.savedQuotesFragment)
        }

        toggleButton = rootView.findViewById(R.id.toggleBtn)

        toggleButton.setOnClickListener{
            if (viewType =="single") {
                manager.beginTransaction().replace(R.id.quoteBody, multiQuoteFragment).commit()
                viewType="multi"
            }
            else if (viewType=="multi"){
                manager.beginTransaction().replace(R.id.quoteBody, singleQuoteFragment).commit()
                viewType="single"
            }
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
                        multiQuoteFragment.resetType("random")
                        RandomFragment()
                    }
                    "By Keyword" ->{
                        currentTab = "keyword"
                        multiQuoteFragment.resetType("keyword")
                        SearchFragment()
                    }
                    "By Author" ->{
                        currentTab = "author"
                        multiQuoteFragment.resetType("author")
                        SearchFragment()
                    }
                    else -> Fragment()
                }).commit()
                if (viewType == "single"){
                    singleQuoteFragment.setBinding(null)
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
            singleQuoteFragment.setBinding(quote)
        })
        quoteVM?.getError()?.observe(viewLifecycleOwner, Observer<VolleyError> { e ->
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        })

        quoteVM!!.fetchRandom()
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

}
