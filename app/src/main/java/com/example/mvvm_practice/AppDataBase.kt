package com.example.mvvm_practice

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.accountmodule.UserDao
import com.example.accountmodule.UserModel

@Database(entities = [UserModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase:RoomDatabase(){
    abstract fun userDao(): UserDao
}
