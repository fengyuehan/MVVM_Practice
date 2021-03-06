package com.example.lib_base.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.network.AutoRegisterNetListener
import com.example.lib_base.network.NetworkStateChangeListener
import com.example.lib_base.utils.*

abstract class BaseActivity<VB:ViewBinding,VM:BaseViewModel> :AppCompatActivity(),FrameView<VB>,NetworkStateChangeListener{

    protected val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        BindingReflex.reflexViewBinding(javaClass, layoutInflater)
    }

    protected abstract val mViewModel:VM

    private var mStatusHelper :ActivityRecreateHelper ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        ARouter.getInstance().inject(this)
        // 注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)

        setStatusBar()
        mBinding.initView()
        initNetworkListener()
        initObserve()
        initRequestData()
    }

    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     */
    override fun networkTypeChange(type: Int) {}

    /**
     * 网络连接状态更改回调
     * @param isConnected Boolean 是否已连接
     * @return Unit
     */
    override fun networkConnectChange(isConnected: Boolean) {
        toast(if (isConnected) "网络已连接" else "网络已断开")
    }

    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = ActivityRecreateHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    private fun initNetworkListener() {
        lifecycle.addObserver(AutoRegisterNetListener(this))
    }

    private fun setStatusBar() {
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, true)
    }

    /**
     * - activity 重建帮助工具类
     */
    private class ActivityRecreateHelper(savedInstanceState: Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(
            this
        )
        super.onDestroy()

    }
}