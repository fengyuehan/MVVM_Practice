package com.example.lib_common

import androidx.viewbinding.ViewBinding
import com.example.lib_base.base.BaseFragment
import com.example.lib_base.base.BaseViewModel

abstract class BaseAndroidFragment<VB :ViewBinding,VM:BaseViewModel> :BaseFragment<VB,VM>() {
}