package com.example.myroomtest.room.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.accountmodule.Dog
import com.example.accountmodule.Owner

data class DogAndOwner(
    @Embedded
    val owner: Owner,
    @Relation(
        parentColumn = "ownerId",//来自owner的唯一key
        entityColumn = "dogOwnerId"//来自Dog的唯一key
    )
    val dog: List<Dog>

)