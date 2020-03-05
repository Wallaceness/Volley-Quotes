package com.example.android.volleydemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="quotes_table")
class Quote(@field:ColumnInfo(name = "message") val message:String,
            @field:ColumnInfo(name="author") val author: String,
            @field:ColumnInfo(name="keywords") val keywords: String,
            @field:ColumnInfo(name="profession") val profession:String,
            @field:ColumnInfo(name="nationality") val nationality: String,
            @field:ColumnInfo(name="authorBirth") val authorBirth:String,
            @field:ColumnInfo(name="authorDeath") val authorDeath:String) {
    @PrimaryKey(autoGenerate = true) var id=0
}