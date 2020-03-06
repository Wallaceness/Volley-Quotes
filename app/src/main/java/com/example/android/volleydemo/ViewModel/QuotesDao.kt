package com.example.android.volleydemo.ViewModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.volleydemo.Quote

@Dao()
interface QuotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(quote: Quote?)

    @Query("Select * from quotes_table")
    fun getAllQuotes() : LiveData<List<Quote>>

}