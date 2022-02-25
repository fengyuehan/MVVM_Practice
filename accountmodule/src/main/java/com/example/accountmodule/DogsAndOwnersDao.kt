package com.example.accountmodule

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single

@Dao
interface DogsAndOwnersDao {
    @Transaction
    @Query("SELECT * FROM Owner")
    fun getDogsAndOwners(): Single<List<OwnerWithDogs>>
}