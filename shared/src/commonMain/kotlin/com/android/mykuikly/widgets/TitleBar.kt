package com.android.mykuikly.widgets

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.Scale
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// 自定义组件,实现TitleBar
internal class TitleBarView : ComposeView<TitleBarViewAttr, TitleBarViewEvent>() {

    override fun createEvent(): TitleBarViewEvent {
        return TitleBarViewEvent()
    }

    override fun createAttr(): TitleBarViewAttr {
        return TitleBarViewAttr()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        // 返回对应布局
        return {

            View {
                attr {
                    size(pagerData.pageViewWidth, 44f)
                    marginTop(pagerData.statusBarHeight)
                    allCenter()
                    backgroundColor(Color.BLUE)
                }

                Image {
                    attr {
                        size(16f, 16f)
                        src(BASE_64)
                        tintColor(Color.WHITE)
                        resizeContain()
                        absolutePosition(left = 15f, top = (44f - 16f) / 2)
                    }
//                    attr {
//                        // 使用 common 目录下的相对路径,必须有size
//                        size(16f, 16f)
//                        resizeContain()
//                        absolutePosition(left = 15f, top = (44f - 16f) / 2)
//                        src(ImageUri.commonAssets("close.png"))
//                    }
                    event {
                        click {
                            ctx.event.clickHandler?.invoke() // 回调给外部
                        }
                    }
                }

                Text {
                    attr {
                        fontWeightBold()
                        fontSize(16f)
                        color(Color.WHITE)
                        text(ctx.attr.title)
                    }
                }

            }
        }
    }

    companion object {
        private const val BASE_64 =
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAASBAMAAAB/WzlGAAAAElBMVEUAAAAAAAAAAAAAAAAAAAAAAADgKxmiAAAABXRSTlMAIN/PELVZAGcAAAAkSURBVAjXYwABQTDJqCQAooSCHUAcVROCHBiFECTMhVoEtRYA6UMHzQlOjQIAAAAASUVORK5CYII="
    }
}


internal class TitleBarViewAttr : ComposeAttr() {
    var title: String = ""
}

internal class TitleBarViewEvent : ComposeEvent() {
    var clickHandler: (() -> Unit)? = null

    fun backIconClick(handler: () -> Unit) {
        clickHandler = handler
    }
}

// 扩展ViewContainer的TitleBar方法,返回自定义的TitleBarView
internal fun ViewContainer<*, *>.TitleBar(init: TitleBarView.() -> Unit) {
    addChild(TitleBarView(), init)
}
