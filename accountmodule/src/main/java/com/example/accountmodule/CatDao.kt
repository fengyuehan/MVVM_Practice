package com.example.accountmodule

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CatDao {
    @Insert
    fun insert(cat: Cat)
}