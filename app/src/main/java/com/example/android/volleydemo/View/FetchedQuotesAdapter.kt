package com.example.android.volleydemo.View

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.model.Quote
import com.example.android.volleydemo.R
import com.example.android.volleydemo.databinding.QuoteItemBinding

class FetchedQuotesAdapter(var fetchedQuotes:ArrayList<Quote>, var parent: FetchedQuotesFragment): RecyclerView.Adapter<FetchedQuotesAdapter.FetchedQuotesHolder>() {

    private var onBottomReachedListener: onBottomReachedListener?=null

    class FetchedQuotesHolder constructor(quoteBinding: QuoteItemBinding): RecyclerView.ViewHolder(quoteBinding.root) {
        var quote: Quote?=null
        var binder = quoteBinding
        lateinit var saveButton:ImageButton

        fun bind(quote: Quote, parent: FetchedQuotesFragment) {
            binder.quote= quote
            (parent.activity as MainActivity).animateQuote(binder.root)
            binder.executePendingBindings()
            saveButton = binder.root.findViewById(R.id.saveBtn)
            val shareButton = binder.root.findViewById<ImageButton>(R.id.shareBtn)

            saveButton.setOnClickListener {
                (parent).saveQuote(quote)
            }

            shareButton.setOnClickListener{
                (parent.activity as MainActivity).shareQuote(quote)
            }
        }
    }

    fun setOnBottomReachedListener(listener: onBottomReachedListener){
        onBottomReachedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FetchedQuotesHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val holderBinding:QuoteItemBinding = DataBindingUtil.inflate(inflater,
            R.layout.quote_item, parent, false)
        return FetchedQuotesHolder(
            holderBinding
        )
    }

    override fun getItemCount(): Int {
        return fetchedQuotes.size
    }

    fun addQuote(quote: Quote){
        fetchedQuotes.add(quote)
        notifyItemInserted(fetchedQuotes.size-1)
    }

    override fun onBindViewHolder(holder: FetchedQuotesHolder, position: Int) {
        if (position>=fetchedQuotes.size-1){
            onBottomReachedListener?.onBottomReached(position)
        }
        holder.bind(fetchedQuotes.get(position), parent)
    }
}