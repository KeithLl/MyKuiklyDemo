package com.android.mykuikly.module

import android.util.Log
import com.tencent.kuikly.core.render.android.export.KuiklyRenderBaseModule
import com.tencent.kuikly.core.render.android.export.KuiklyRenderCallback

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction: Native侧定义Module,注册的时候必须和Kuikly侧名字一样
 */
class KRMyLogModule : KuiklyRenderBaseModule() {
    companion object {
        const val MODULE_NAME = "KRMyLogModule"

    }

    // KuiklyModule的toNative方法最终调用这个方法
    override fun call(method: String, params: Any?, callback: KuiklyRenderCallback?): Any? {
        val content = (params as? Map<String, Any>)?.get("content") ?: ""
        when (method) {
            "log" -> {
                Log.e("Keith", "log : $content")
            }

            "logWithCallback" -> {
                Log.e("Keith", "logWithCallback : $content")
                callback?.invoke(mapOf("result" to "success log : $content"))
            }

            else -> super.call(method, params, callback)
        }
        return super.call(method, params, callback)
    }
}
