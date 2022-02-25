package com.example.accountmodule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cat(val catName:String){
    @PrimaryKey(autoGenerate = true)
    var catId: Long = 0
    var catOwnerId: Long=2
}
