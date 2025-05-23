package com.android.mykuikly.widgets

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// 自定义组件,Medal头部
internal class MedalTitleView : ComposeView<MedalTitleViewAttr, MedalTitleViewEvent>() {

    override fun createEvent(): MedalTitleViewEvent {
        return MedalTitleViewEvent()
    }

    override fun createAttr(): MedalTitleViewAttr {
        return MedalTitleViewAttr()
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

                Text {
                    attr {
                        fontWeightBold()
                        fontSize(22f)
                        color(Color.WHITE)
                        text(ctx.attr.title)
                        absolutePosition(left = 26f, top = 66f)
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
