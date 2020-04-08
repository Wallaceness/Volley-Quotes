package com.example.android.volleydemo.ViewModel

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.ResultReceiver
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.android.volley.Response
import com.example.android.volleydemo.Constants
import com.example.android.volleydemo.Quote
import com.example.android.volleydemo.R
import com.example.android.volleydemo.View.MainActivity
import org.json.JSONObject


class AlertWorker(appContext: Context, val workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    val vm = QuoteViewModel(applicationContext as Application)
    val requestCode = 0

    override suspend fun doWork(): Result {
        val author = workerParams.inputData.getString("AUTHOR")
        val keyword = workerParams.inputData.getString("KEYWORD")
        val title=if (author!=null){"$author Once Said: "} else if (keyword!=null)"$keyword Quote" else "Random Quote"
        lateinit var result:Result

        val successListener = Response.Listener<JSONObject>{ quote->
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
            intent.putExtra("notification_quote", Quote(quote.optString("message"),
                quote.optString("author"),
                quote.optString("keywords"),
                quote.optString("profession"),
                quote.optString("nationality"),
                quote.optString("authorBirth"),
                quote.optString("authorDeath")))
            val pending = PendingIntent.getActivity(applicationContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notificationBuilder= NotificationCompat.Builder(applicationContext, "QUOTES_CHANNEL")
                .setSmallIcon(R.drawable.ic_format_quote_black_24dp)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle()
                .bigText(quote.optString("message")))
                .setContentIntent(pending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            val notifier = NotificationManagerCompat.from(applicationContext)
            notifier.notify(1, notificationBuilder.build())
//            val data= Bundle()
//            data.putString("AUTHORKEYWORD", keyword?:author)
//            data.putString("TYPE", workerParams.inputData.getString("TYPE"))
//            data.putString("FREQUENCY", workerParams.inputData.getString("FREQUENCY"))
            result= Result.success()
//            resultReceiver.send(Constants.Success_Result, data)
        }
        val errorListener = Response.ErrorListener { error->
            result=Result.failure()
            val data = Bundle()
            data.putString("ErrorMessage", error.message)
//            resultReceiver.send(Constants.Failure_Result, data)
        }
        if (author!=null){
            vm.fetchByAuthor(author, successListener, errorListener)
        }
        else if (keyword!=null){
            vm.fetchByKeyword(keyword, successListener, errorListener)
        }
        else{
            vm.fetchRandom(successListener, errorListener)
        }

        return result
    }
}