package com.example.android.volleydemo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Integer.parseInt

@Entity(tableName="quotes_table")
class Quote(@PrimaryKey @field:ColumnInfo(name = "message") val message:String,
            @field:ColumnInfo(name="author") val author: String,
            @field:ColumnInfo(name="keywords") val keywords: String,
            @field:ColumnInfo(name="profession") val profession:String,
            @field:ColumnInfo(name="nationality") val nationality: String,
            @field:ColumnInfo(name="authorBirth") val authorBirth:String,
            @field:ColumnInfo(name="authorDeath") val authorDeath:String):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    fun formatAuthorLife():String{
        var string= "-"+author
        if (authorBirth != "null"){
            string+=", "+authorBirth!!.split("-")[2]
            if (authorDeath!="null"){
                string+= " - ${authorDeath!!.split("-")[2]}"
            }
            else string+=" - Present"
        }
        return string
    }

    fun formatDate(date:String):String{
        if (date!="null") {
            val months = hashMapOf<String, String>()
            months["Jan"] = "January"
            months["Feb"] = "February"
            months["Mar"] = "March"
            months["Apr"] = "April"
            months["May"] = "May"
            months["Jun"] = "June"
            months["Jul"] = "July"
            months["Aug"] = "August"
            months["Sep"] = "September"
            months["Oct"] = "October"
            months["Nov"] = "November"
            months["Dec"] = "December"
            val parse = date.split("-")
            return "${months[parse[1]] ?: ""} ${parseInt(parse[0])} ${parse[2]}"
        }
        else return ""
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(message)
        dest?.writeString(author)
        dest?.writeString(keywords)
        dest?.writeString(profession)
        dest?.writeString(nationality)
        dest?.writeString(authorBirth)
        dest?.writeString(authorDeath)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quote> {
        override fun createFromParcel(parcel: Parcel): Quote {
            return Quote(parcel)
        }

        override fun newArray(size: Int): Array<Quote?> {
            return arrayOfNulls(size)
        }
    }
}