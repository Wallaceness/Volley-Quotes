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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.example.android.volleydemo.databinding.FragmentMainBinding
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
    private var dataBinder: FragmentMainBinding? = null
    var quoteVM: QuoteViewModel?=null
    var quote: Quote?=null
    lateinit var tabs:TabLayout
    var currentTab:String = "random"
    lateinit var saveButton:ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        QuoteViewModel.VolleyQueue.init(requireActivity()!!.applicationContext)
        dataBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        val rootView = dataBinder?.root
        quoteVM = QuoteViewModel(requireActivity().application)
        tabs = rootView!!.findViewById(R.id.tabLayout)
        val manager = childFragmentManager
        manager.beginTransaction().add(R.id.controlPanel, RandomFragment()).commit()
        saveButton = rootView.findViewById(R.id.saveBtn)

        saveButton.setOnClickListener {
            quoteVM?.saveQuote(this.quote!!)
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
                        RandomFragment()
                    }
                    "By Keyword" ->{
                        currentTab = "keyword"
                        SearchFragment()
                    }
                    "By Author" ->{
                        currentTab = "author"
                        SearchFragment()
                    }
                    else -> Fragment()
                }).commit()

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
            this.quote=quote
            dataBinder?.quote = quote
        })

        quoteVM!!.fetchRandom()
        return rootView
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
