package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.example.android.volleydemo.databinding.QuoteItemBinding
import java.util.zip.Inflater

/**
 * A simple [Fragment] subclass.
 */
class SingleQuoteFragment : Fragment() {
    var databinder: QuoteItemBinding? = null
    lateinit var saveButton:ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databinder = DataBindingUtil.inflate(inflater,R.layout.quote_item, container, false)
        val rootView= databinder?.root
        databinder?.saved = false
        saveButton = rootView!!.findViewById(R.id.saveBtn)

        saveButton.setOnClickListener {
            (parentFragment as MainFragment).quoteVM?.saveQuote(databinder?.quote!!)
        }
        return rootView
    }

    fun setBinding(quote:Quote?){
        databinder?.quote = quote
    }

}
