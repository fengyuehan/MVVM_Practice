package com.example.mvvm_practice

import android.content.Context
import androidx.multidex.MultiDex
import com.example.accountmodule.AccountModuleRoomAccessor
import com.example.accountmodule.UserDao
import com.example.lib_base.BaseApplication
import com.example.lib_common.CommonApplication
import dagger.hilt.android.HiltAndroidApp
import org.greenrobot.eventbus.EventBus

@HiltAndroidApp
class AppApplication: BaseApplication() {

    override fun onCreate() {
        // 开启EventBusAPT,优化反射效率 当组件作为App运行时需要将添加的Index注释掉 因为找不到对应的类了
        EventBus
            .builder()
//            .addIndex(MainEventIndex())
            .installDefaultEventBus()
        super.onCreate()

        AccountModuleRoomAccessor.onGetDaoCallback = object :AccountModuleRoomAccessor.OnGetDaoCallback{
            override fun onGetUserDao(): UserDao {
                return DBHelper.db.userDao()
            }

        }


    }
}