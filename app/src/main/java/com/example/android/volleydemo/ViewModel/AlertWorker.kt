package com.example.android.volleydemo.ViewModel

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.android.volley.Response
import com.example.android.volleydemo.R
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
            val intent = Intent()
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pending = PendingIntent.getActivity(applicationContext, requestCode, intent, 0)
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
            val data=workDataOf("AUTHORKEYWORD" to (keyword?:author),
                "TYPE" to workerParams.inputData.getString("TYPE"),
                "FREQUENCY" to workerParams.inputData.getString("FREQUENCY"))
            result= Result.success(data)
        }
        val errorListener = Response.ErrorListener { error->
            result=Result.failure()
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