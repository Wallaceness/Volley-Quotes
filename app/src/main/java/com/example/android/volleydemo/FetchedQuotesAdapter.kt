package com.example.android.volleydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.volleydemo.databinding.QuoteItemBinding

class FetchedQuotesAdapter(var fetchedQuotes:ArrayList<Quote>, var parent:FetchedQuotesFragment): RecyclerView.Adapter<FetchedQuotesAdapter.FetchedQuotesHolder>() {

    private var onBottomReachedListener:onBottomReachedListener?=null

    class FetchedQuotesHolder constructor(quoteBinding: QuoteItemBinding): RecyclerView.ViewHolder(quoteBinding.root) {
        var quote:Quote?=null
        var binder = quoteBinding
        lateinit var saveButton:ImageButton

        fun bind(quote:Quote, parent:FetchedQuotesFragment) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 1000
            binder.quote= quote
            binder.root.startAnimation(fadeIn)
            binder.executePendingBindings()
            saveButton = binder.root.findViewById(R.id.saveBtn)

            saveButton.setOnClickListener {
                (parent).saveQuote(quote)
            }
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
        notifyItemInserted(fetchedQuotes.size-1)
    }

    override fun onBindViewHolder(holder: FetchedQuotesHolder, position: Int) {
        if (position>=fetchedQuotes.size-1){
            onBottomReachedListener?.onBottomReached(position)
        }
        holder.bind(fetchedQuotes.get(position), parent)
    }
}