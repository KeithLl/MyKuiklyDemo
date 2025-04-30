package com.android.mykuikly.modules

import com.tencent.kuikly.core.module.CallbackFn
import com.tencent.kuikly.core.module.Module

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction: 自定义LogModule
 */
class MyLogModule : Module() {
    override fun moduleName(): String {
        return "KRMyLogModule"
    }

    fun log(content: String) {
        // 调用Native层的方法
        toNative(
            false, // 回调常驻
            "log", // 方法名
//            content,
            mapOf(
                "content" to content
            ), // 参数
            null, // 回调到Kuikly的函数
            false, // 是否同步调用
        )
    }

    fun logWithCallback(content: String, callbackFn: CallbackFn) {
        toNative(
            true, // 回调常驻
            "logWithCallback", // 方法名
//            content,
            mapOf(
                "content" to content
            ), // 参数
            callbackFn, // 回调到Kuikly的函数
            false, // 是否同步调用
        )
    }
}
