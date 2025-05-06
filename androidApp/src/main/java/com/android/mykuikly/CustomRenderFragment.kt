package com.android.mykuikly

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.android.mykuikly.adapter.KRColorParserAdapter
import com.android.mykuikly.adapter.KRFontAdapter
import com.android.mykuikly.adapter.KRImageAdapter
import com.android.mykuikly.adapter.KRLogAdapter
import com.android.mykuikly.adapter.KRRouterAdapter
import com.android.mykuikly.adapter.KRThreadAdapter
import com.android.mykuikly.adapter.KRUncaughtExceptionHandlerAdapter
import com.tencent.kuikly.core.render.android.adapter.KuiklyRenderAdapterManager
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegator
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegatorDelegate
import org.json.JSONObject

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction:自定义页面
 */
class CustomRenderFragment : Fragment() , KuiklyRenderViewBaseDelegatorDelegate {
    private val kuiklyRenderViewDelegator = KuiklyRenderViewBaseDelegator(this)

    companion object {

        private const val KEY_PAGE_NAME = "pageName"
        private const val KEY_PAGE_DATA = "pageData"

        init {
            initKuiklyAdapter()
        }

        fun start(context: Context, pageName: String, pageData: JSONObject) {
            val starter = Intent(context, KuiklyRenderActivity::class.java)
            starter.putExtra(KEY_PAGE_NAME, pageName)
            starter.putExtra(KEY_PAGE_DATA, pageData.toString())
            context.startActivity(starter)
        }

        private fun initKuiklyAdapter() {
            with(KuiklyRenderAdapterManager) {
                krImageAdapter = KRImageAdapter
                krLogAdapter = KRLogAdapter
                krUncaughtExceptionHandlerAdapter = KRUncaughtExceptionHandlerAdapter
                krFontAdapter = KRFontAdapter
                krColorParseAdapter = KRColorParserAdapter(KRApplication.application)
                krRouterAdapter = KRRouterAdapter
                krThreadAdapter = KRThreadAdapter()
            }
        }
    }
}
