package com.example.lib_base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class BaseApplication :MultiDexApplication() {

    private val mCoroutineScope by lazy { MainScope() }
    private val mLoadModuleProxy by lazy { LoadModuleProxy() }

    companion object{
        lateinit var context: Context

        lateinit var application: Application
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = this
        application = this
        mLoadModuleProxy.onAttachBaseContext(context)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
        mLoadModuleProxy.onCreate(this)
        // 策略初始化第三方依赖
        initDepends()
    }

    private fun initDepends() {
        mCoroutineScope.launch(Dispatchers.Default){
            mLoadModuleProxy.initByBackstage()
        }

        val allTimeMillis = measureTimeMillis {
            val depends = mLoadModuleProxy.initByFrontDesk()
            var dependInfo :String
            depends.forEach{
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
                Log.d("BaseApplication", "initDepends: $dependInfo : $dependTimeMillis ms")
            }
        }
        Log.d("BaseApplication", "初始化完成 $allTimeMillis ms")
    }

    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }
}