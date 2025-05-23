package com.android.mykuikly.widgets

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.NotifyModule
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.timer.CallbackRef
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import kotlin.getValue

// 自定义组件,Medal头部
internal class MedalTitleView : ComposeView<MedalTitleViewAttr, MedalTitleViewEvent>() {

    private val notifyModule by lazy(LazyThreadSafetyMode.NONE) {
        acquireModule<NotifyModule>(NotifyModule.MODULE_NAME)
    }
    private var titleText by observable("快去获取徽章吧")
    private var titleUrl by observable("")
    private lateinit var notifyRef: CallbackRef

    override fun createEvent(): MedalTitleViewEvent {
        return MedalTitleViewEvent()
    }

    override fun createAttr(): MedalTitleViewAttr {
        return MedalTitleViewAttr()
    }

    override fun viewDidLoad() {
        super.viewDidLoad()

        notifyRef = notifyModule.addNotify("onMedalListPageDidMount") {
            KLog.e("Keith", "$it, ${it?.optJSONObject("name")}")
            titleText = "${it?.optString("name")}"
            titleUrl = "${it?.optString("url")}"
        }
    }

    override fun viewDidUnload() {
        notifyModule.removeNotify("onMedalListPageDidMount", notifyRef)
        super.viewDidUnload()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        // 返回对应布局
        return {

            View {
                attr {
                    size(pagerData.pageViewWidth, 170f)
                    alignItemsFlexStart()
                    backgroundImage(ImageUri.pageAssets("all_medal_head_bg.png").toString())
                }

                Image {
                    attr {
                        size(pagerData.pageViewWidth, 170f)
                        src(ImageUri.pageAssets("all_medal_head_bg.png"))
                        resizeCover()
                        absolutePosition(left = 0f, top = 0f, right = 0f, bottom = 0f)
                    }
                }

                Image {
                    attr {
                        size(114f, 140f)
                        src(ImageUri.pageAssets("all_medal_head_icon_bg.png"))
                        absolutePosition(top = 0f, right = 20f)
                    }
                }

                Image {
                    attr {
                        size(88f, 88f)
                        src(ctx.titleUrl)
                        absolutePosition(top = 40f, right = 33f)
                    }
                }

                Text {
                    attr {
                        fontWeightBold()
                        fontSize(22f)
                        color(Color.WHITE)
                        text(ctx.titleText)
                        absolutePosition(left = 26f, top = 66f)
                    }
                }

            }
        }
    }

    companion object {}
}


internal class MedalTitleViewAttr : ComposeAttr() {
    var title: String = ""
}

internal class MedalTitleViewEvent : ComposeEvent() {
    var clickHandler: (() -> Unit)? = null

    fun backIconClick(handler: () -> Unit) {
        clickHandler = handler
    }
}

// 扩展ViewContainer的TitleBar方法,返回自定义的MedalTitleView
internal fun ViewContainer<*, *>.MedalTitleView(init: MedalTitleView.() -> Unit) {
    addChild(MedalTitleView(), init)
}
