package com.example.accountmodule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owner(
    val name: String

) {
    @PrimaryKey
    var ownerId: Long = 2
}