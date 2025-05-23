package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.android.mykuikly.base.setTimeout
import com.android.mykuikly.beans.MedalBean
import com.android.mykuikly.beans.MedalResultBean
import com.android.mykuikly.modules.MyLogModule
import com.android.mykuikly.widgets.MedalItemView
import com.android.mykuikly.widgets.MedalTitleView
import com.android.mykuikly.widgets.TitleBar
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Animation
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.Scale
import com.tencent.kuikly.core.base.Translate
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.base.event.EventHandlerFn
import com.tencent.kuikly.core.directives.vforIndex
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.Module
import com.tencent.kuikly.core.module.NetworkModule
import com.tencent.kuikly.core.module.NotifyModule
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.collection.ObservableList
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.reactive.handler.observableList
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Modal
import com.tencent.kuikly.core.views.Refresh
import com.tencent.kuikly.core.views.RefreshView
import com.tencent.kuikly.core.views.RefreshViewState
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.core.views.WaterfallList
import com.tencent.kuikly.core.views.compose.Button


@Page("MedalList")
internal class MedalListPage : BasePager() {
    var dataList: ObservableList<MedalBean> by observableList<MedalBean>()
    var dataResult by observable(MedalResultBean(JSONObject()))
    private lateinit var refreshRef: ViewRef<RefreshView>
    private var refreshText by observable("下拉刷新")
    private var selectMedalItem: MedalBean? by observable(null)

    private val notifyModule by lazy(LazyThreadSafetyMode.NONE) {
        acquireModule<NotifyModule>(NotifyModule.MODULE_NAME)
    }


    override fun created() {
        super.created()
        pagerData

        getPageData()
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

            MedalTitleView { attr { title = ctx.dataResult.wearMedal?.name ?: "" } }

//            Text {
//                attr {
//                    fontWeightBold()
//                    fontSize(18f)
//                    color(0xff333333)
//                    text("${ctx.wearingMedal.name}")
//                    marginLeft(26f)
//                    fontWeightNormal()
//                    positionAbsolute()
//                    top(116f)
//                }
//            }

            Text {
                attr {
                    fontWeightBold()
                    fontSize(18f)
                    color(0xff666666)
                    text("全部勋章(${ctx.dataResult.grantCount}/${ctx.dataResult.totalCount})")
                    marginLeft(26f)
                    fontWeightNormal()
                }

            }

            vif({ ctx.selectMedalItem != null }) {
                Modal {
                    ActionSheet {
                        attr {
                            medalItem = ctx.selectMedalItem!!
                        }
                        event {
                            close {
                                KLog.e("Keith", "22224343434434 close")
                                ctx.selectMedalItem = null
                            }
                        }
                    }
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

                Refresh {
                    ref {
                        ctx.refreshRef = it
                    }
                    attr {
                        height(50f)
                        backgroundColor(Color.GRAY)
                        allCenter()
                    }
                    event {
                        refreshStateDidChange {
                            when (it) {
                                RefreshViewState.REFRESHING -> {
                                    ctx.refreshText = "正在刷新"
                                    ctx.getPageData()
                                    setTimeout(1000) {
                                        ctx.refreshRef.view?.endRefresh()
                                        ctx.refreshText = "刷新成功"
                                    }
                                }

                                RefreshViewState.IDLE -> ctx.refreshText = "下拉刷新"
                                RefreshViewState.PULLING -> ctx.refreshText = "松手即可刷新"
                            }
                        }
                    }
                    Text {
                        attr {
                            color(Color.BLACK)
                            fontSize(18f)
                            text(ctx.refreshText)
                        }
                    }
                }

                vforIndex({ ctx.dataList }) { item, index, _ ->
                    MedalItemView {
                        attr {
//                            backgroundColor(item.bgColor)
                            borderRadius(8f)
                            medalItem = item
                        }

                        event {
                            click {
                                KLog.e("Keith", "click")
                                ctx.selectMedalItem = item
                            }
                        }
                    }
                }
            }
        }
    }

    fun getPageData() {
        acquireModule<NetworkModule>(NetworkModule.MODULE_NAME).httpRequest(
            "https://qaaiclass.knowbox.cn/api/v3/medal/redirect?source=androidAiClassNew&s=androidAiClassNew&version=2.2.0&token=2cd8eb18-90f0-40b2-86d3-fd669bef5bda&appVersion=5.2.38&platform=Android&appName=androidAiClass&deviceVersion=13&deviceType=V2278A",
            true,
            param = JSONObject("{\"source\":\"androidAiClassNew\",\"appVersion\":5238,\"version\":\"5.2.38\",\"channel\":\"Knowbox\",\"platform\":\"Android\",\"appName\":\"androidAiClass\",\"apiVersion\":\"3\",\"deviceVersion\":\"13\",\"deviceType\":\"V2278A\",\"token\":\"2cd8eb18-90f0-40b2-86d3-fd669bef5bda\",\"s\":\"androidAiClassNew\",\"path\":\"\\/getAllMedal\",\"params\":\"{}\"}"),
            headers = JSONObject().apply {
                put("Content-Type", "application/json")
            },
            responseCallback = { data, success, errorMsg ->
                val resultBean = MedalResultBean(data.optJSONObject("data") ?: JSONObject())
                dataResult = resultBean

                notifyModule.postNotify(
                    eventName = "onMedalListPageDidMount", eventData = JSONObject().apply {
                        put("name", dataResult.wearMedal?.name ?: "")
                        put("url", dataResult.wearMedal?.activeIcon ?: "")
                    })

                dataList.clear()
                dataList.addAll(resultBean.medalList)
            })
    }

    // 监听页面事件
    override fun onReceivePagerEvent(pagerEvent: String, eventData: JSONObject) {
        super.onReceivePagerEvent(pagerEvent, eventData)
    }
}


internal class ActionSheetView : ComposeView<ActionSheetViewAttr, ActionSheetEvent>() {
    var animated: Boolean by observable(false)
    override fun body(): ViewBuilder {
        var ctx = this
        return {
            attr {
                absolutePosition(0f, 0f, 0f, 0f)
                justifyContentCenter()
                if (ctx.animated) {
                    backgroundColor(Color(0, 0, 0, 0.5f))
                } else {
                    backgroundColor(Color(0, 0, 0, 0f))
                }
                animation(Animation.springEaseIn(0.5f, 0.92f, 1f), ctx.animated)

                // 三生三世

            }

            event {
                click {
                    ctx.animated = false
                }
                animationCompletion {
                    if (!ctx.animated) {
                        KLog.e("Keith", "22224343434434 animationCompletion")
                        this@ActionSheetView.emit(ActionSheetEvent.CLOSE, it)
                    }
                }
            }

            View {
                attr {
                    backgroundColor(Color.WHITE)
                    borderRadius(12f)
                    padding(10f)
                    if (ctx.animated) {
                        transform(Translate(0f, 0f))
                    } else {
                        transform(Scale(1f, 0f))
                    }
                    marginRight(50f)
                    marginLeft(50f)
                    allCenter()
                    animation(Animation.springEaseIn(0.5f, 0.92f, 1f), ctx.animated)
                }


                Image {
                    attr {
                        size(180f, 180f)
                        src(if (ctx.attr.medalItem.isGranted) ctx.attr.medalItem.activeIcon else ctx.attr.medalItem.icon)
                    }
                }

                Text {
                    attr {
                        fontWeightBold()
                        fontSize(24f)
                        marginTop(10f)
                        color(Color.BLACK)
                        text(ctx.attr.medalItem.name)
                        lines(1)
                    }
                }

            }
        }
    }

    override fun createAttr(): ActionSheetViewAttr {
        return ActionSheetViewAttr()
    }

    override fun createEvent(): ActionSheetEvent {
        return ActionSheetEvent()
    }

    override fun viewDidLayout() {
        super.viewDidLayout()
        animated = true
    }

}

internal class ActionSheetViewAttr : ComposeAttr() {
    lateinit var medalItem: MedalBean
}

internal class ActionSheetEvent : ComposeEvent() {
    fun close(handler: EventHandlerFn) {
        registerEvent(CLOSE, handler)
    }

    companion object {
        const val CLOSE = "close"
    }
}

internal fun ViewContainer<*, *>.ActionSheet(init: ActionSheetView.() -> Unit) {
    addChild(ActionSheetView(), init)
}
