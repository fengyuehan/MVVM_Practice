package com.example.lib_common

import android.app.Application
import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.ApplicationLifecycle
import com.example.lib_base.BaseApplication
import com.example.lib_base.VersionStatus
import com.example.lib_base.network.NetworkStateClient
import com.example.lib_base.utils.ProcessUtils
import com.example.lib_base.utils.SpUtils
import com.google.auto.service.AutoService
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

@AutoService(ApplicationLifecycle::class)
open class CommonApplication :ApplicationLifecycle{

    companion object{
        lateinit var mCommonApplication: CommonApplication
    }


    override fun onAttachBaseContext(context: Context) {
        mCommonApplication = this
    }

    override fun onCreate(application: Application) {
        TODO("Not yet implemented")
    }

    override fun onTerminate(application: Application) {
        TODO("Not yet implemented")
    }

    override fun initByFrontDesk(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        // 以下只需要在主进程当中初始化 按需要调整
        if (ProcessUtils.isMainProcess(BaseApplication.context)) {
            list.add { initMMKV() }
            list.add { initARouter() }
            list.add { initNetworkStateClient() }
        }
        list.add { initTencentBugly() }
        return list
    }

    /**
     * 初始化网络状态监听客户端
     * @return Unit
     */
    private fun initNetworkStateClient(): String {
        NetworkStateClient.register()
        return "NetworkStateClient -->> init complete"
    }

    /**
     * 腾讯TBS WebView X5 内核初始化
     */
    private fun initX5WebViewCore() {
        // dex2oat优化方案
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        // 允许使用非wifi网络进行下载
        QbSdk.setDownloadWithoutWifi(true)

        // 初始化
        QbSdk.initX5Environment(BaseApplication.context, object : QbSdk.PreInitCallback {

            override fun onCoreInitFinished() {
                Log.d("ApplicationInit", " TBS X5 init finished")
            }

            override fun onViewInitFinished(p0: Boolean) {
                // 初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核
                Log.d("ApplicationInit", " TBS X5 init is $p0")
            }
        })
    }

    /**
     * 腾讯 MMKV 初始化
     */
    private fun initMMKV(): String {
        val result = SpUtils.initMMKV(BaseApplication.context)
        return "MMKV -->> $result"
    }

    /**
     * 阿里路由 ARouter 初始化
     */
    private fun initARouter(): String {
        // 测试环境下打开ARouter的日志和调试模式 正式环境需要关闭
        if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(BaseApplication.application)
        return "ARouter -->> init complete"
    }

    /**
     * 初始化 腾讯Bugly
     * 测试环境应该与正式环境的日志收集渠道分隔开
     * 目前有两个渠道 测试版本/正式版本
     */
    private fun initTencentBugly(): String {
        // 第三个参数为SDK调试模式开关
        CrashReport.initCrashReport(
            BaseApplication.context,
            BaseApplication.context.getString(R.string.BUGLY_APP_ID),
            BuildConfig.VERSION_TYPE != VersionStatus.RELEASE
        )
        return "Bugly -->> init complete"
    }

    override fun initByBackstage() {
        initX5WebViewCore()
    }


}