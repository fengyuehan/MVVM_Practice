package com.example.mvvm_practice

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lib_base.BaseApplication.Companion.application


object DBHelper {
    private const val TAG = "DBHelper"
    private const val DB_PREFIX = "test_info"

    private val dataBase = Room.databaseBuilder(application,AppDataBase::class.java, DB_PREFIX)
        .addCallback(object :RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d(TAG, "Room database onCreate in thread " + Thread.currentThread().name)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "Room database onOpen in thread " + Thread.currentThread().name)
            }
        }).fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

        val db :AppDataBase by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ dataBase }
}