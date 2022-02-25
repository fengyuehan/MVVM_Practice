package com.example.accountmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myroomtest.room.SchedulerUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var dogId = 0L
    var owenId = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AccountModuleRoomAccessor.getUserDao()

        //添加够
        add_dog.setOnClickListener {
            dogId++
            AccountModuleRoomAccessor.getDogDao().insert(
                Dog(
                    name = "狗名字$dogId",
                    breed = "asdsa",
                    cuteness = 9,
                    barkVolume = 98
                )
            )

        }
//
        //添加毛
        add_cat.setOnClickListener {
            AccountModuleRoomAccessor.getCatDao().insert(
                Cat(
                    catName = "猫"
                )
            )
        }

        //添加人
        add_owen.setOnClickListener {
            owenId++
            AccountModuleRoomAccessor.getOwnerDao().insert(Owner("人名$owenId"))
        }

        get_all.setOnClickListener {
            AccountModuleRoomAccessor.getDogsAndOwnersDao().getDogsAndOwners()
                ?.compose(SchedulerUtils.ioToMain())
                ?.subscribe({

                    it.forEach {

                    }

                    Log.e("messagr", "${it.toString()}=======")
                }, {
                    Log.e("messagr", "$it")
                })
        }
    }
}