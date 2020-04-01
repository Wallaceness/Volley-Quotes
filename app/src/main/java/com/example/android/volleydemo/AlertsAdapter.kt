package com.example.android.volleydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo

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
        holder.type.text = parsed.get(1)
        holder.authorKeyword.text =parsed.get(2)
        holder.frequency.text = parsed.get(3)
    }

    class AlertHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var type:TextView
        lateinit var authorKeyword: TextView
        lateinit var frequency:TextView

        init{
            type = itemView.findViewById(R.id.typeView)
            authorKeyword = itemView.findViewById(R.id.keywordOrAuthorView)
            frequency = itemView.findViewById(R.id.frequencyView)
        }
    }
}