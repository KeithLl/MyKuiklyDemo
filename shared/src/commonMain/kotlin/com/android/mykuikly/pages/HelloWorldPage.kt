package com.android.mykuikly.pages

import com.android.mykuikly.base.BasePager
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.views.Text

@Page("HelloWorld")
internal class HelloWorldPage : BasePager() {

    override fun body(): ViewBuilder {
        return {
            attr {
                allCenter()
            }

            Text {
                attr {
                    text("Hello Kuikly Page")
                    fontSize(14f)
                }
            }
        }
    }
}
