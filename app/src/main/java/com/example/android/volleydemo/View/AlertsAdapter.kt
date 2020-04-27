package com.example.android.volleydemo.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.example.android.volleydemo.R

class AlertsAdapter(list:ArrayList<WorkInfo>):RecyclerView.Adapter<AlertsAdapter.AlertHolder>() {
    var alertsList:ArrayList<WorkInfo> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return AlertHolder(view)
    }

    override fun getItemCount(): Int {
        return alertsList.size
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        val info = alertsList[position]
        val tags= info.tags
        lateinit var parsed:List<String>
        tags.forEach { string->
            if (string.indexOf("INFO_")!=-1){
                parsed=string.split("_")
            }
        }
        holder.workInfo = info
        holder.type.text = parsed.get(1)
        holder.authorKeyword.text =if (parsed.get(2)=="null") "-" else parsed.get(2)
        holder.frequency.text = parseFrequency(parsed.get(3))
    }

    fun parseFrequency(time:String): String{
        var seconds= Integer.parseInt(time)
        var reps = 0
        while (seconds>=60){
            seconds/=60
            reps+=1
        }
        val unit = when(reps){
            1->"Minutes"
            2->"Hours"
            else-> ""
        }

        return "$seconds $unit"
    }

    class AlertHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var type:TextView
        lateinit var authorKeyword: TextView
        lateinit var frequency:TextView
        val delete: ImageButton = itemView.findViewById(R.id.deleteButton)
        lateinit var workInfo:WorkInfo

        init{
            type = itemView.findViewById(R.id.typeView)
            authorKeyword = itemView.findViewById(R.id.keywordOrAuthorView)
            frequency = itemView.findViewById(R.id.frequencyView)
            delete.setOnClickListener {
                itemView.findFragment<SettingsFragment>().launchDelete(workInfo)
//                val deleteModal = DeleteAlert({it->WorkManager.getInstance(itemView.context).cancelWorkById(workInfo.id)}
//                    , "Are you sure you want to delete this notification task?")
            }
        }
    }
}