package com.android.mykuikly.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import com.tencent.kuikly.core.render.android.adapter.IKRRouterAdapter
import com.android.mykuikly.KuiklyRenderActivity
import com.android.mykuikly.MainActivity
import org.json.JSONObject

object KRRouterAdapter : IKRRouterAdapter {

    override fun openPage(
        context: Context,
        pageName: String,
        pageData: JSONObject,
    ) {
        KuiklyRenderActivity.start(context, pageName, pageData)
    }

    override fun closePage(context: Context) {
        Log.e("Keith", "closePage, $context")
        if (context is MainActivity) {
            (context as MainActivity).openNewPage()
            return
        }
        (context as? Activity)?.finish()
    }
}
