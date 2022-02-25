package com.example.accountmodule

import androidx.room.Embedded
import androidx.room.Relation

data class OwnerWithDogs(@Embedded val owner: Owner,
                         @Relation(
                             parentColumn = "ownerId",
                             entityColumn = "dogOwnerId"
                         )
                         val dogs: List<Dog>,
                         @Relation(
                             parentColumn = "ownerId",
                             entityColumn = "catOwnerId"
                         )
                         val cats: List<Cat>)
