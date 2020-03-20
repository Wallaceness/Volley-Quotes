package com.example.android.volleydemo.ViewModel

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.volleydemo.Quote
import com.example.android.volleydemo.SavedQuotesDB
import com.example.android.volleydemo.Secrets
import org.json.JSONObject

class QuoteViewModel(@NonNull application: Application) : AndroidViewModel(Application()) {
    private val quote : MutableLiveData<JSONObject> = MutableLiveData()
    private val error: MutableLiveData<VolleyError> = MutableLiveData()
    lateinit var savedQuotesDb: SavedQuotesDB
    private lateinit var savedQuotes: LiveData<List<Quote>>


    companion object VolleyQueue {
        var requestQueue: RequestQueue? = null
        fun init(
            context: Context
        ) {
            requestQueue = Volley.newRequestQueue(context)
        }
    }

    init{
        savedQuotesDb = SavedQuotesDB.getDB(application.applicationContext)!!
        savedQuotes = savedQuotesDb.quoteDao().getAllQuotes()
    }

    val base = "https://150000-quotes.p.rapidapi.com/"
    private val successListener = Response.Listener<JSONObject> {response->
        quote.postValue(response)
    }
    private val errorListener = Response.ErrorListener {error ->
        this.error.postValue(error)
    }

    fun fetchByKeyword(term: String) {
        val url = "${base}keyword/${term}"
        val request = object : JsonObjectRequest(url, null, successListener, errorListener) {

            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        requestQueue?.add(request)
    }

    fun fetchRandom() {
        val request = object: JsonObjectRequest(Request.Method.GET, base + "random", null,
            successListener, errorListener
        ){
            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        requestQueue?.add(request)
    }

    fun fetchByAuthor(author: String) {
        val request = object: JsonObjectRequest(Request.Method.GET, base + "author/" + author, null,
            successListener, errorListener
        ){
            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        requestQueue!!.add(request)
    }

    fun getQuote(): LiveData<JSONObject>{
        return quote
    }

    fun getError(): LiveData<VolleyError>{
        return error
    }

    fun getSavedQuotes(): LiveData<List<Quote>>{
        return savedQuotes
    }

    fun saveQuote(quote: Quote){
        SavedQuotesDB.databaseWriteExecutor.execute({ savedQuotesDb.quoteDao().insert(quote) })
    }

    fun deleteQuote(quote: Quote){
        SavedQuotesDB.databaseWriteExecutor.execute({savedQuotesDb.quoteDao().delete(quote)})
    }
}
