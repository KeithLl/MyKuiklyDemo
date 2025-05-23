package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.android.mykuikly.modules.MyLogModule
import com.android.mykuikly.widgets.MedalItemView
import com.android.mykuikly.widgets.MedalTitleView
import com.android.mykuikly.widgets.TitleBar
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.BaseObject
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.directives.vforIndex
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.Module
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.collection.ObservableList
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.reactive.handler.observableList
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.core.views.WaterfallList


internal class WaterFallItem : BaseObject() {
    var title: String by observable("")
    var bgColor: Color by observable(Color.WHITE)
    var height: Float by observable(0f)
}

@Page("MedalList")
internal class MedalListPage : BasePager() {
    var dataList: ObservableList<WaterFallItem> by observableList<WaterFallItem>()

    override fun created() {
        super.created()
        val pageData = pagerData

        for (index in 0..1000) {
            dataList.add(WaterFallItem().apply {
                title = "我是第${this@MedalListPage.dataList.size + 1}个卡片"
                height = 200f
                bgColor = Color((0..255).random(), (0..255).random(), (0..255).random(), 1.0f)
            })
        }
    }

    // 注册外部的Module,会在created之前调用
    override fun createExternalModules(): Map<String, Module>? {
        KLog.e("Keith", "createExternalModules")
        return mapOf(
            "KRMyLogModule" to MyLogModule(),
        )
    }

    override fun pageWillDestroy() {
        super.pageWillDestroy()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            attr {
                flexDirectionColumn()
            }

            TitleBar {
                attr {
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

            MedalTitleView { attr { title = "Medal Title" } }

            Text {
                attr {
                    fontWeightBold()
                    fontSize(18f)
                    color(0xff666666)
                    text("全部勋章(0/0)")
                    marginLeft(26f)
                    fontWeightNormal()
                }
            }

            WaterfallList {
                attr {
                    flex(1f)
                    // columnCount((pagerData.pageViewWidth / 180f).toInt())
                    columnCount(3)
                    listWidth(pagerData.pageViewWidth)
                    lineSpacing(10f)
                    itemSpacing(10f)
                    padding(10f)
                }

                vforIndex({ ctx.dataList }) { item, index, _ ->
                    MedalItemView{
                        attr {
                            backgroundColor(item.bgColor)
                            borderRadius(8f)
                            name = item.title
                        }
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
