package com.example.android.volleydemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest

@Entity(tableName="quotes_table")
class Quote(@PrimaryKey @field:ColumnInfo(name = "message") val message:String,
            @field:ColumnInfo(name="author") val author: String,
            @field:ColumnInfo(name="keywords") val keywords: String,
            @field:ColumnInfo(name="profession") val profession:String,
            @field:ColumnInfo(name="nationality") val nationality: String,
            @field:ColumnInfo(name="authorBirth") val authorBirth:String,
            @field:ColumnInfo(name="authorDeath") val authorDeath:String) {

    fun formatAuthorLife():String{
        var string= "-"+author
        if (authorBirth != "null"){
            string+=", "+authorBirth.split("-")[2]
            if (authorDeath!="null"){
                string+= " - ${authorDeath.split("-")[2]}"
            }
            else string+=" - Present"
        }
        return string
    }
}