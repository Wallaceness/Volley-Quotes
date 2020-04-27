package com.example.android.volleydemo.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.example.android.volleydemo.model.Quote
import com.example.android.volleydemo.R
import com.example.android.volleydemo.databinding.QuoteItemBinding

/**
 * A simple [Fragment] subclass.
 */
class SingleQuoteFragment(var quote: Quote?=null) : Fragment() {
    var databinder: QuoteItemBinding? = null
    lateinit var saveButton:ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databinder = DataBindingUtil.inflate(inflater,
            R.layout.quote_item, container, false)
        val rootView= databinder?.root
        databinder?.saved = false
        saveButton = rootView!!.findViewById(R.id.saveBtn)
        val shareButton = rootView.findViewById<ImageButton>(R.id.shareBtn)

        saveButton.setOnClickListener {
            (parentFragment as MainFragment).quoteVM?.saveQuote(databinder?.quote!!)
        }

        shareButton.setOnClickListener {
            (activity as MainActivity).shareQuote(databinder?.quote!!)
        }
        if (this.quote!=null){
            setBinding(this.quote)
        }
        return rootView
    }

    fun setBinding(quote: Quote?){
        databinder?.quote = quote
        if (quote!=null){
            //animation happens here
            (activity as MainActivity).animateQuote(databinder?.root)
        }
        databinder?.root?.alpha = 1f
    }


}
