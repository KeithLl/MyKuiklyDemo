package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.android.mykuikly.base.setTimeout
import com.android.mykuikly.widgets.TitleBar
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.module.NotifyModule
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text

@Page("Test")
internal class TestMyPage : BasePager() {

    override fun created() {
        super.created()
        KLog.e("Keith", pagerData.params.toString())

        if (pagerData.params.has("testKey")) {
            KLog.e("Keith", "testKey value is " + pagerData.params.optString("testKey"))
        }
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            attr {
                allCenter()
            }
            TitleBar {
                attr {
                    absolutePosition(top = 0f)
                    title = "Test Page Title"
                }
                event {
                    backIconClick {
                        // 返回键点击事件
                        KLog.e("Keith", "test back click")
                        ctx.acquireModule<RouterModule>(RouterModule.MODULE_NAME).closePage()
                    }
                }
            }
            Text {
                attr {
                    text("Test My Page")
                    fontSize(18f)
                    color(Color.BLUE)
                    textDecorationUnderLine()
                }
                event {
                    click {
                        setTimeout(500) {
                            KLog.e("Keith", "test page text click")
                            ctx.acquireModule<NotifyModule>(NotifyModule.MODULE_NAME)
                                .postNotify("test", JSONObject().apply {
                                    put("test", 1)
                                })
                        }
                    }
                }
            }

            Image {
                attr {
                    size(pagerData.pageViewWidth, 200f)
                    backgroundColor(color = Color.GREEN)
                    // 使用 common 目录下的相对路径
//                    src(ImageUri.commonAssets("all_pass.png"))//访问公共资源
                    src(ImageUri.pageAssets("all_pass_1.png"))// 访问当前页面的资源
//                    src(ImageUri.pageAssets("close.png"))
                }
            }
        }
    }
}
