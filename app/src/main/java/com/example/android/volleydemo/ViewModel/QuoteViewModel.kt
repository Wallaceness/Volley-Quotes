package com.example.android.volleydemo.ViewModel

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.android.volleydemo.Quote
import com.example.android.volleydemo.SavedQuotesDB
import com.example.android.volleydemo.Secrets
import com.example.android.volleydemo.View.MainActivity
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class QuoteViewModel(@NonNull application: Application) : AndroidViewModel(Application()) {
    private val quote : MutableLiveData<JSONObject> = MutableLiveData()
    private val error: MutableLiveData<VolleyError> = MutableLiveData()
    lateinit var savedQuotesDb: SavedQuotesDB
    private lateinit var savedQuotes: LiveData<List<Quote>>
    private val AUTHOR_KEY = "AUTHOR"
    private val KEYWORD_KEY = "KEYWORD"
    private val TYPE_KEY = "TYPE"
    private val FREQUENCY_KEY = "FREQUENCY"
    val app = application


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

    fun fetchByKeyword(term: String, succListener:Response.Listener<JSONObject> = successListener, errListener:Response.ErrorListener = errorListener) {
        if (term!="") {
            var term = term.substring(0, 1).toUpperCase() + term.substring(1)
            val url = "${base}keyword/${term}"
            val request = object : JsonObjectRequest(url, null, succListener, errListener) {

                override fun getHeaders(): MutableMap<String, String> =
                    hashMapOf<String, String>().apply {
                        put("x-rapidapi-host", Secrets.ApiHost)
                        put("x-rapidapi-key", Secrets.ApiKey)
                    }
            }
            requestQueue?.add(request)
        }
    }

    fun fetchRandom(succListener:Response.Listener<JSONObject> = successListener, errListener:Response.ErrorListener = errorListener) {
        val request = object: JsonObjectRequest(Request.Method.GET, base + "random", null,
            succListener, errListener
        ){
            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        requestQueue?.add(request)
    }

    fun fetchByAuthor(author: String, succListener:Response.Listener<JSONObject> = successListener, errListener:Response.ErrorListener = errorListener) {
        if (author != "") {
            var term = author.split(" ")
            term = term.map { it.substring(0, 1).toUpperCase() + it.substring(1) }
            var result = term.joinToString(separator = "%20")
            val request = object : JsonObjectRequest(
                Request.Method.GET, base + "author/" + result, null,
                succListener, errListener
            ) {
                override fun getHeaders(): MutableMap<String, String> =
                    hashMapOf<String, String>().apply {
                        put("x-rapidapi-host", Secrets.ApiHost)
                        put("x-rapidapi-key", Secrets.ApiKey)
                    }
            }
            requestQueue!!.add(request)
        }
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

    fun createAlert(keyword:String?, author: String?, frequency:Int, type:String) {
        val data: Data = workDataOf(
            KEYWORD_KEY to keyword,
            AUTHOR_KEY to author, TYPE_KEY to type, FREQUENCY_KEY to frequency
        )

        val alertRequest =
            PeriodicWorkRequestBuilder<AlertWorker>(frequency.toLong(), TimeUnit.SECONDS)
        alertRequest.setInputData(data)
            .addTag("QuoteAlert")
            .addTag("INFO_${type}_${author?:keyword}_$frequency")

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            "Alert" + keyword + author + frequency,
            ExistingPeriodicWorkPolicy.REPLACE, alertRequest.build()
        )
    }
}
