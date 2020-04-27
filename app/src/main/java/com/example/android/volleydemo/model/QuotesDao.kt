package com.example.android.volleydemo.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao()
interface QuotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(quote: Quote?)

    @Query("Select * from quotes_table")
    fun getAllQuotes() : LiveData<List<Quote>>

    @Delete
    fun delete(quote: Quote)

}