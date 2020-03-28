package com.example.android.volleydemo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.example.android.volleydemo.Secrets
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AppSettingViewModel(application: Application) : AndroidViewModel(application) {

    private val NumberOfThreads = 5
    private val quote : MutableLiveData<JSONObject> = MutableLiveData()
    private val error: MutableLiveData<VolleyError> = MutableLiveData()
    private val AUTHOR_KEY = "AUTHOR"
    private val KEYWORD_KEY = "KEYWORD"

    val base = "https://150000-quotes.p.rapidapi.com/"
    private val successListener = Response.Listener<JSONObject> { response->
        quote.postValue(response)
    }
    private val errorListener = Response.ErrorListener { error ->
        this.error.postValue(error)
    }

    fun fetchByKeyword(term: String) {
        var term = term.substring(0, 1).toUpperCase()+term.substring(1)
        val url = "${base}keyword/${term}"
        val request = object : JsonObjectRequest(url, null, successListener, errorListener) {

            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        QuoteViewModel.requestQueue?.add(request)
    }

    fun fetchRandom() {
        val request = object: JsonObjectRequest(
            Request.Method.GET, base + "random", null,
            successListener, errorListener
        ){
            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        QuoteViewModel.requestQueue?.add(request)
    }

    fun fetchByAuthor(author: String) {
        var term = author.split(" ")
        term = term.map {it.substring(0, 1).toUpperCase()+it.substring(1)}
        var result = term.joinToString(separator = " ")
        val request = object: JsonObjectRequest(
            Request.Method.GET, base + "author/" + result, null,
            successListener, errorListener
        ){
            override fun getHeaders(): MutableMap<String, String> =
                hashMapOf<String, String>().apply {
                    put("x-rapidapi-host", Secrets.ApiHost)
                    put("x-rapidapi-key", Secrets.ApiKey)
                }
        }
        QuoteViewModel.requestQueue!!.add(request)
    }

    fun getQuote(): LiveData<JSONObject> {
        return quote
    }

    fun getError(): LiveData<VolleyError> {
        return error
    }

    fun createAlert(keyword:String?, author: String?, frequency:Int){
        val data:Data = workDataOf(KEYWORD_KEY to keyword,
                                    AUTHOR_KEY to author)
        val alertRequest = PeriodicWorkRequestBuilder<AlertWorker>(frequency.toLong(), TimeUnit.SECONDS)
        alertRequest.setInputData(data)
        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork("Alert"+keyword+author+frequency,ExistingPeriodicWorkPolicy.REPLACE, alertRequest.build())
    }

}