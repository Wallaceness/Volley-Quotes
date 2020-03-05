package com.example.android.volleydemo

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SavedQuotesAdapter constructor(savedQuotes: ArrayList<Quote>): RecyclerView.Adapter<SavedQuotesAdapter.SavedQuoteHolder> () {
    lateinit var savedQuotes: ArrayList<Quote>

    init{
        this.savedQuotes = savedQuotes
    }

    class SavedQuoteHolder(itemView: View, quote: Quote) : RecyclerView.ViewHolder(itemView) {
        var quote: Quote = quote

        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuoteHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return savedQuotes.size
    }

    override fun onBindViewHolder(holder: SavedQuoteHolder, position: Int) {

    }
}