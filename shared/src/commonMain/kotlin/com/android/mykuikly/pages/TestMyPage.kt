package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.views.Text

@Page("Test")
internal class TestMyPage : BasePager() {

    override fun body(): ViewBuilder {
        return {
            attr {
                allCenter()
            }

            Text {
                attr {
                    text("Test My Page")
                    fontSize(18f)
                    color(Color.BLUE)
                    textDecorationUnderLine()
                }
            }
        }
    }
}
