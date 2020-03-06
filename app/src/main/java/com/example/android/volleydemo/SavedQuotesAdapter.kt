package com.example.android.volleydemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.databinding.QuoteItemBinding

class SavedQuotesAdapter constructor(savedQuotes: ArrayList<Quote>, context:Context): RecyclerView.Adapter<SavedQuotesAdapter.SavedQuoteHolder> () {
    var savedQuotes: ArrayList<Quote>

    init{
        this.savedQuotes = savedQuotes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuoteHolder {
        var inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val holderBinding:QuoteItemBinding = DataBindingUtil.inflate(inflater, R.layout.quote_item, parent, false)
        return SavedQuoteHolder(holderBinding)
    }

    override fun getItemCount(): Int {
        return savedQuotes.size
    }

    override fun onBindViewHolder(holder: SavedQuoteHolder, position: Int) {
        val item:Quote = savedQuotes.get(position)
        holder.bind(item)
    }

    fun updateQuotes(quotes: ArrayList<Quote>){
        savedQuotes = quotes
        notifyDataSetChanged()
    }

    class SavedQuoteHolder(quoteBinding: QuoteItemBinding) : RecyclerView.ViewHolder(quoteBinding.root) {
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
}