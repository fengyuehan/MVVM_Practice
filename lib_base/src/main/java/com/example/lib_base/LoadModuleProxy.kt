package com.example.lib_base

import android.app.Application
import android.content.Context
import java.util.*

class LoadModuleProxy :ApplicationLifecycle {
    private var mLoader:ServiceLoader<ApplicationLifecycle> = ServiceLoader.load(ApplicationLifecycle::class.java)

    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            it.onAttachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        mLoader.forEach {
            it.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        mLoader.forEach {
            it.onTerminate(application)
        }
    }

    override fun initByFrontDesk(): MutableList<() -> String> {
        val list:MutableList<() -> String> = mutableListOf()
        mLoader.forEach {
            list.addAll(it.initByFrontDesk())
        }
        return list
    }

    override fun initByBackstage() {
        mLoader.forEach {
            it.initByBackstage()
        }
    }
}