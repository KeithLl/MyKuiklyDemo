package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.android.mykuikly.widgets.TitleBar
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.CallbackRef
import com.tencent.kuikly.core.module.MemoryCacheModule
import com.tencent.kuikly.core.module.NotifyModule
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.TextView
import com.tencent.kuikly.core.views.compose.Button

@Page("HelloWorld")
internal class HelloWorldPage : BasePager() {


    // 页面数据
    private var counter by observable(0)

    // 获取对应组件
    lateinit var textRef: ViewRef<TextView>

    // 监听事件
    lateinit var eventCallbackRef: CallbackRef

    override fun created() {
        super.created()
        val pageData = pagerData
        KLog.e("Keith", "HelloWorldPage created, pageData: $pageData")

        // 获取Module
        // 1. 通过acquireModule<T>(moduleName)获取Module, 如果找不到Module的话会抛异常
//        val cacheModule = acquireModule<BridgeModule>(BridgeModule.MODULE_NAME)
//        cacheModule.toast("hello world")


        // 2. getModule<T>(moduleName)获取Module, 如果找不到Module的话返回null
        val cacheModule1 = getModule<MemoryCacheModule>(MemoryCacheModule.MODULE_NAME)
        cacheModule1?.setObject("test", "test")

        // 添加事件监听
        eventCallbackRef = acquireModule<NotifyModule>(NotifyModule.MODULE_NAME).addNotify("test") { data ->
            // data参数为发送方传递过来的参数
            // 事件处理
            KLog.e("Keith", "receive test event, data: $data")
        }
    }

    override fun pageWillDestroy() {
        super.pageWillDestroy()
        // 移除事件监听
        acquireModule<NotifyModule>(NotifyModule.MODULE_NAME).removeNotify("test", eventCallbackRef)
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            attr {
                backgroundColor(Color.WHITE)
                justifyContentCenter()
                alignItemsCenter()
            }

            TitleBar {
                attr {
                    absolutePosition(top = 0f)
                    title = "Hello World Title"
                }
                event {
                    backIconClick {
                        // 返回键点击事件
                        KLog.e("Keith", "back click")
                        ctx.acquireModule<RouterModule>(RouterModule.MODULE_NAME).closePage()
                    }
                }
            }

            Text {
                ref {
                    ctx.textRef = it
                }
                attr {
                    minSize(60f, 20f)
                    text(ctx.counter.toString())
                    fontSize(20f)
                    textAlignCenter()
                    color(Color.WHITE)
                    fontWeightBold()
                    backgroundColor(
                        if (ctx.counter % 2 == 0) {
                            Color.RED
                        } else {
                            Color.GRAY
                        }
                    )
                }

                event {
                    click {
                        ctx.acquireModule<RouterModule>(RouterModule.MODULE_NAME).openPage(
                            "Test",
                            JSONObject().apply {
                                put("testKey", "testValue")
                            })
                    }
                }
            }

            Image {
                attr {
                    size(pagerData.pageViewWidth, 200f)
                    backgroundColor(color = Color.GREEN)
                    // 使用 common 目录下的相对路径
//                    src(ImageUri.commonAssets("all_pass.png"))
                    src(ImageUri.pageAssets("all_pass_1.png"))
//                    src(ImageUri.pageAssets("close.png"))
                }
            }

            Button {
                attr {
                    absolutePosition(bottom = 30f, right = 30f)
                    size(80f, 80f)

                    borderRadius(10f)
                    backgroundColor(Color.BLUE)

                    titleAttr {
                        text("点击更新")
                        fontSize(15f)
                        color(Color.WHITE)
                        fontWeightBold()
                    }
                }

                event {
                    click {
                        ctx.counter++
                    }
                }
            }
        }
    }

    // 监听页面事件
    override fun onReceivePagerEvent(pagerEvent: String, eventData: JSONObject) {
        super.onReceivePagerEvent(pagerEvent, eventData)
    }
}
