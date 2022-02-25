package com.example.accountmodule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dog(
    val name: String,
    val cuteness: Int,
    val barkVolume: Int,
    val breed: String

) {
    @PrimaryKey(autoGenerate = true)
    var dogId: Long=0
    var dogOwnerId: Long=2

}
