package com.example.accountmodule

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface OwnerDao {
    @Insert
    fun insert(owner: Owner)
}