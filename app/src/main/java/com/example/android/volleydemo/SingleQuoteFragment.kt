package com.example.android.volleydemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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
        //animation for fading in
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000
        databinder?.quote = quote
        if (quote!=null){
            databinder?.root?.startAnimation(fadeIn)
        }
        databinder?.root?.alpha = 1f
    }

}
