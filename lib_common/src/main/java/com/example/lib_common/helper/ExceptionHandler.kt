package com.quyunshuo.wanandroid.common.helper

import com.example.lib_base.utils.toast
import kotlin.jvm.Throws
import com.quyunshuo.wanandroid.common.helper.ResponseExceptionEnum as ExceptionType

/**
 * 请求异常处理
 *
 * 该方法主要做两件事:
 *
 * - 1.做统一的code码处理
 * - 2.未进行统一处理的code码会被转换为自定义异常[ResponseException]抛出
 *
 * 使用方式为：进行统一处理的异常进行抛出[ResponseEmptyException]，未进行处理的抛出[ResponseException]
 *
 * @throws ResponseException 未进行处理的异常会进行抛出，让ViewModel去做进一步处理
 */
@Throws(ResponseException::class)
fun responseCodeExceptionHandler(code: Int, msg: String) {
    // 进行异常的处理
    when (code) {
        ExceptionType.NOT_LOGIN_ERROR.getCode() -> {
            toast(ExceptionType.NOT_LOGIN_ERROR.getMessage())
            throw ResponseEmptyException()
        }
        ExceptionType.SUCCESS.getCode() ->{ }
        else -> {
            throw ResponseException(ExceptionType.ERROR, msg)
        }
    }
}