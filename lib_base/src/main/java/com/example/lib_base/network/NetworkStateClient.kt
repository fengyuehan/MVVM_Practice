package com.example.lib_base.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.example.lib_base.BaseApplication

object NetworkStateClient {
    private val mNetworkCallbackImpl = NetworkCallbackImpl()

    /**
     * 注册网络监听客户端
     * @return Unit
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun register(){
        val build = NetworkRequest.Builder().build()
        val cm = BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(build, mNetworkCallbackImpl)
    }

    /**
     * 设置监听器
     * @param listener NetworkStateChangeListener 监听器
     * @return Unit
     */
    fun setListener(listener: NetworkStateChangeListener){
        mNetworkCallbackImpl.changeCall = listener
    }

    /**
     * 移除监听器
     * @return Unit
     */
    fun removeListener(){
        mNetworkCallbackImpl.changeCall = null
    }

    /**
     * 获取网络类型 其他类型的网络归为了WIFI，如果需要细分，可以对[NetworkCallbackImpl]进行追加分类
     * @return Int WIFI:[NetworkCapabilities.TRANSPORT_WIFI]、移动网络:[NetworkCapabilities.TRANSPORT_CELLULAR]
     */
    fun getNetworkType(): Int = mNetworkCallbackImpl.currentNetworkType

    /**
     * 网络是否连接
     * @return Boolean
     */
    fun isConnected(): Boolean = mNetworkCallbackImpl.isConnected
}