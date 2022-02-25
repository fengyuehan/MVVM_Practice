package com.example.accountmodule

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DogDao {
    @Insert
    fun insert(dog: Dog)
}