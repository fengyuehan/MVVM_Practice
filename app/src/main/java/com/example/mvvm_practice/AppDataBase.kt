package com.example.mvvm_practice

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.accountmodule.*

@Database(entities = [UserModel::class,Owner::class,Dog::class,Cat::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase:RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun getDao(): DogsAndOwnersDao
    abstract fun getDog(): DogDao
    abstract fun getOwen(): OwnerDao
    abstract fun getCat(): CatDao
}
