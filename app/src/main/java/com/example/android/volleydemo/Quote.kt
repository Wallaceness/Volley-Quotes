package com.example.android.volleydemo

import android.os.Parcel
import android.os.Parcelable
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