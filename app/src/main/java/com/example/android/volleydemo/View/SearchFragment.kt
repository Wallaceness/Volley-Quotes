package com.example.android.volleydemo.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.android.volleydemo.R

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment constructor(var searchField:String="") : Fragment() {
    lateinit var textarea:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)
        val searchBtn: Button = rootView.findViewById(R.id.searchBtn)
        val mvm = (parentFragment as MainFragment).quoteVM
        textarea= rootView.findViewById(R.id.searchTxt)
        textarea.setText(searchField)

        searchBtn.setOnClickListener { v: View ->
            val searchValue = textarea.text.toString()
            val type = (parentFragment as MainFragment).currentTab
            val viewType=(parentFragment as MainFragment).viewType
            if (viewType=="single"){
                if (type == "author") {
                    mvm?.fetchByAuthor(searchValue)
                } else if (type == "keyword") {
                    mvm?.fetchByKeyword(searchValue)
                }
            }
            else{
                (parentFragment as MainFragment).startMulti()
            }
        }

        textarea.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s!=null){
                    (parentFragment as MainFragment).newSearchValue(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        return rootView
    }

}
