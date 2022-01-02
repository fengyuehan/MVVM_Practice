package com.example.lib_common

import android.util.Log
import androidx.viewbinding.ViewBinding
import com.example.lib_base.ActivityStackManager
import com.example.lib_base.base.BaseActivity
import com.example.lib_base.base.BaseViewModel
import com.example.lib_base.utils.AndroidBugFixUtils

abstract class BaseAndroidActivity<VB :ViewBinding,VM:BaseViewModel> :BaseActivity<VB,VM>() {
    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "ActivityStack: ${ActivityStackManager.activityStack}")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解决某些特定机型会触发的Android本身的Bug
        AndroidBugFixUtils().fixSoftInputLeaks(this)
    }
}