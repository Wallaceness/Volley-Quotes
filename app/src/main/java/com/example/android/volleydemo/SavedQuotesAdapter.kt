package com.example.android.volleydemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.ViewModel.QuoteViewModel
import com.example.android.volleydemo.databinding.QuoteItemBinding
import kotlin.jvm.internal.CallableReference

class SavedQuotesAdapter constructor(savedQuotes: ArrayList<Quote>, context: SavedQuotesFragment): RecyclerView.Adapter<SavedQuotesAdapter.SavedQuoteHolder> () {
    var savedQuotes: ArrayList<Quote>
    val parentContext=context

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
        holder.bind(item, parentContext)
    }

    fun updateQuotes(quotes: ArrayList<Quote>){
        savedQuotes = quotes
        notifyDataSetChanged()
    }

    class SavedQuoteHolder(quoteBinding: QuoteItemBinding) : RecyclerView.ViewHolder(quoteBinding.root) {
        var quote:Quote?=null
        var binder:QuoteItemBinding
        lateinit var deleteButton:ImageButton

         init{
             binder = quoteBinding
             binder.saved = true
        }

        fun bind(quote:Quote, context:SavedQuotesFragment) {
            binder.quote= quote
            binder.executePendingBindings()
            deleteButton= binder.root.findViewById(R.id.saveBtn)

            deleteButton.setOnClickListener(View.OnClickListener {
                context.launchDialog(quote)
            })
        }
    }
}