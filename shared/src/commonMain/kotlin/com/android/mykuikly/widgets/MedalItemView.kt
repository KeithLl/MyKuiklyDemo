package com.android.mykuikly.widgets

import com.android.mykuikly.beans.MedalBean
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.nvi.serialization.ISerialization
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// 自定义组件,Medal头部
internal class MedalItemView : ComposeView<MedalItemViewAttr, MedalItemViewEvent>() {

    override fun createEvent(): MedalItemViewEvent {
        return MedalItemViewEvent()
    }

    override fun createAttr(): MedalItemViewAttr {
        return MedalItemViewAttr()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        // 返回对应布局
        return {

            View {
                attr {
                    alignItemsCenter()
                    flexDirectionColumn()
                    padding(top = 10f, bottom = 10f)
                }

                Image {
                    attr {
                        size(80f, 80f)
                        src(if (ctx.attr.medalItem.isGranted) ctx.attr.medalItem.activeIcon else ctx.attr.medalItem.icon)
                    }
                }

                Text {
                    attr {
                        fontWeightBold()
                        fontSize(14f)
                        color(Color.BLACK)
                        text(ctx.attr.medalItem.name)
                        lines(1)
                        lineHeight(20f)
                    }
                }

            }
        }
    }

    companion object {
    }
}


internal class MedalItemViewAttr : ComposeAttr() {
    lateinit var medalItem: MedalBean
}

internal class MedalItemViewEvent : ComposeEvent() {
    var clickHandler: (() -> Unit)? = null

    fun backIconClick(handler: () -> Unit) {
        clickHandler = handler
    }
}

// 扩展ViewContainer的TitleBar方法,返回自定义的MedalItemView
internal fun ViewContainer<*, *>.MedalItemView(init: MedalItemView.() -> Unit) {
    addChild(MedalItemView(), init)
}
