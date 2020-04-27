package com.example.android.volleydemo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class SavedQuotesDB:RoomDatabase() {
    abstract fun quoteDao(): QuotesDao
    //create an instance of QuotesDao interface

    //make a static instance of the database
    companion object{
        @Volatile
        private var INSTANCE : SavedQuotesDB?=null
        private val NumberOfThreads = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NumberOfThreads)

        fun getDB(context: Context): SavedQuotesDB?{
            //create your database instance if it doesn't already exist
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    SavedQuotesDB::class.java,
                    "saved_quotes_DB")
                    .build()
            }
            return INSTANCE
        }
    }

    //add callback to execute after creation of db, if needed

}