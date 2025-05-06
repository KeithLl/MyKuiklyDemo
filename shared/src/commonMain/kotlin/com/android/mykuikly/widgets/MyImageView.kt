package com.android.mykuikly.widgets

import com.tencent.kuikly.core.base.Attr
import com.tencent.kuikly.core.base.DeclarativeBaseView
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.event.Event
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction: 自定义图片组件
 */
class MyImageView : DeclarativeBaseView<MyImageViewAttr, MyImageViewEvent>() {
    override fun createAttr(): MyImageViewAttr {
        return MyImageViewAttr()
    }

    override fun createEvent(): MyImageViewEvent {
        return MyImageViewEvent()
    }

    override fun viewName(): String {
        return "MyImageView" // 对应Native侧的名字
    }

    // 自定义测试的方法,组件暴露给原生实现
    fun test() {
        performTaskWhenRenderViewDidLoad {
            renderView?.callMethod("test", "params")
            // 也支持异步返回callback结果,不支持同步回调
//            renderView?.callMethod("test", "params", callback = {
//                println("test callback : $it")
//            })
        }
    }
}

class MyImageViewAttr : Attr() {

    /**
     * 设置src
     * @param src 数据源
     * @return this
     */
    fun src(src: String): MyImageViewAttr {
        "src" with src
        return this
    }
}

class MyImageViewEvent : Event() {
    /*
     * 图片成功加载时回调
     * 由原生侧的组件触发
     */
    fun loadSuccess(handler: (LoadSuccessParams) -> Unit) {
        register(LOAD_SUCCESS) {
            handler(LoadSuccessParams.decode(it))
        }
    }
    companion object {
        const val LOAD_SUCCESS = "loadSuccess"
    }
}

data class LoadSuccessParams(
    val src: String
) {
    companion object {
        fun decode(params: Any?): LoadSuccessParams {
            val tempParams = params as? JSONObject ?: JSONObject()
            val src = tempParams.optString("src", "")
            return LoadSuccessParams(src)
        }
    }
}

fun ViewContainer<*, *>.MyImageView(init: MyImageView.() -> Unit) {
    addChild(MyImageView(), init)
}
