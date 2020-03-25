package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        val searchBtn: Button = rootView.findViewById(R.id.searchBtn)
        val textarea:EditText = rootView.findViewById(R.id.searchTxt)
        val mvm = (parentFragment as MainFragment).quoteVM

        searchBtn.setOnClickListener { v: View ->
            val searchValue = textarea.text.toString()
            val type = (parentFragment as MainFragment).currentTab
            if (type == "author") {
                mvm?.fetchByAuthor(searchValue)
            } else if (type == "keyword") {
                mvm?.fetchByKeyword(searchValue)
            }
            (parentFragment as MainFragment).newSearchValue(searchValue)
        }
        return rootView
    }

}
