package com.example.lib_common

import com.google.gson.annotations.SerializedName

data class BaseResponse<D>(
    val `data` :D,
    @SerializedName("errorCode")val code:Int ,
    @SerializedName("errormsg")val msg:String
)
