package com.example.android.volleydemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.databinding.QuoteItemBinding

class FetchedQuotesAdapter: RecyclerView.Adapter<FetchedQuotesAdapter.FetchedQuotesHolder>() {

    private var fetchedQuotes: ArrayList<Quote> = ArrayList()
    private var onBottomReachedListener:onBottomReachedListener?=null

    class FetchedQuotesHolder constructor(quoteBinding: QuoteItemBinding): RecyclerView.ViewHolder(quoteBinding.root) {
        var quote:Quote?=null
        var binder:QuoteItemBinding

        init{
            binder = quoteBinding
        }

        fun bind(quote:Quote) {
            binder.quote= quote
            binder.executePendingBindings()
        }
    }

    fun setOnBottomReachedListener(listener: onBottomReachedListener){
        onBottomReachedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FetchedQuotesHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val holderBinding:QuoteItemBinding = DataBindingUtil.inflate(inflater, R.layout.quote_item, parent, false)
        return FetchedQuotesHolder(holderBinding)
    }

    override fun getItemCount(): Int {
        return fetchedQuotes.size
    }

    fun addQuote(quote:Quote){
        fetchedQuotes.add(quote)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FetchedQuotesHolder, position: Int) {
        if (position>=fetchedQuotes.size-1){
            onBottomReachedListener?.onBottomReached(position)
        }
        holder.bind(fetchedQuotes.get(position))
    }
}