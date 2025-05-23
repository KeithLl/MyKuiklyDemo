package com.android.mykuikly.pages.medal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.mykuikly.KRApplication
import com.android.mykuikly.R
import com.android.mykuikly.adapter.KRColorParserAdapter
import com.android.mykuikly.adapter.KRFontAdapter
import com.android.mykuikly.adapter.KRImageAdapter
import com.android.mykuikly.adapter.KRLogAdapter
import com.android.mykuikly.adapter.KRRouterAdapter
import com.android.mykuikly.adapter.KRThreadAdapter
import com.android.mykuikly.adapter.KRUncaughtExceptionHandlerAdapter
import com.android.mykuikly.databinding.FragmentMedalListBinding
import com.android.mykuikly.module.KRBridgeModule
import com.android.mykuikly.module.KRMyLogModule
import com.android.mykuikly.module.KRShareModule
import com.android.mykuikly.widgets.HRMyImageView
import com.tencent.kuikly.core.render.android.IKuiklyRenderExport
import com.tencent.kuikly.core.render.android.adapter.KuiklyRenderAdapterManager
import com.tencent.kuikly.core.render.android.css.ktx.toMap
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegator
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegatorDelegate
import com.tencent.kuikly.core.render.android.expand.module.getKuiklyEventName
import com.tencent.kuikly.core.render.android.expand.module.getKuiklyEventParams
import com.tencent.kuikly.core.render.android.expand.module.registerKuiklyBroadcastReceiver
import com.tencent.kuikly.core.render.android.expand.module.unregisterKuiklyBroadcastReceiver
import org.json.JSONObject

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction:自定义页面
 */
//commonParams: "?source=androidAiClassNew&s=androidAiClassNew&version=2.2.0&token=2cd8eb18-90f0-40b2-86d3-fd669bef5bda&appVersion=5.2.38&platform=Android&appName=androidAiClass&deviceVersion=13&deviceType=V2278A"
//method: "post"
//pageName: "AllMedalFragment"
//params: "{\"source\":\"androidAiClassNew\",\"appVersion\":5238,\"version\":\"5.2.38\",\"channel\":\"Knowbox\",\"platform\":\"Android\",\"appName\":\"androidAiClass\",\"apiVersion\":\"3\",\"deviceVersion\":\"13\",\"deviceType\":\"V2278A\",\"token\":\"2cd8eb18-90f0-40b2-86d3-fd669bef5bda\",\"s\":\"androidAiClassNew\",\"path\":\"\\/getAllMedal\",\"params\":\"{}\"}"
//url: "https://qaaiclass.knowbox.cn/api/v3/medal/redirect?source=androidAiClassNew&s=androidAiClassNew&version=2.2.0&token=2cd8eb18-90f0-40b2-86d3-fd669bef5bda&appVersion=5.2.38&platform=Android&appName=androidAiClass&deviceVersion=13&deviceType=V2278A"

class MedalListFragment : Fragment(), KuiklyRenderViewBaseDelegatorDelegate {
    private val kuiklyRenderViewDelegator = KuiklyRenderViewBaseDelegator(this)
    var pageName = ""
    private lateinit var llRootPage: ViewGroup
    private lateinit var viewBinding : FragmentMedalListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
         viewBinding = FragmentMedalListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageName = arguments?.getString(KEY_PAGE_NAME) ?: ""
        Log.e("Keith", "pageName is : $pageName")
        llRootPage = view.findViewById(R.id.ll_root_page)
        kuiklyRenderViewDelegator.onAttach(llRootPage, "", pageName, createPageData())
        activity?.registerKuiklyBroadcastReceiver(kuiklyReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        kuiklyRenderViewDelegator.onDetach()
        activity?.unregisterKuiklyBroadcastReceiver(kuiklyReceiver)
    }

    override fun onPause() {
        super.onPause()
        kuiklyRenderViewDelegator.onPause()
    }

    override fun onResume() {
        super.onResume()
        kuiklyRenderViewDelegator.onResume()
    }

    val kuiklyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val eventName = intent.getKuiklyEventName() // 接收到的事件名字
            val data = intent.getKuiklyEventParams() // kuikly侧传递的参数
            Log.e(
                "Keith",
                "Receive event in Fragment , pageName is : ${pageName}: $eventName, data: $data"
            )
        }
    }

    override fun registerExternalModule(kuiklyRenderExport: IKuiklyRenderExport) {
        super.registerExternalModule(kuiklyRenderExport)
        // 注册模块
        Log.e("Keith", "Register Module $kuiklyRenderExport")
        with(kuiklyRenderExport) {
            moduleExport(KRBridgeModule.Companion.MODULE_NAME) {
                KRBridgeModule()
            }
            moduleExport(KRShareModule.Companion.MODULE_NAME) {
                KRShareModule()
            }
            Log.e("Keith", "Register Module inner ${KRMyLogModule.Companion.MODULE_NAME}")
            moduleExport(KRMyLogModule.Companion.MODULE_NAME) {
                KRMyLogModule()
            }
        }
    }

    override fun registerExternalRenderView(kuiklyRenderExport: IKuiklyRenderExport) {
        super.registerExternalRenderView(kuiklyRenderExport)
        // 注册对应的View到Kuikly
        with(kuiklyRenderExport) {
            renderViewExport("MyImageView", { context ->
                HRMyImageView(context)
            })
        }
    }

    private fun createPageData(): Map<String, Any> {
        val param = argsToMap()
        param["appId"] = 1
        return param
    }

    private fun argsToMap(): MutableMap<String, Any> {
        val jsonStr = arguments?.getString(KEY_PAGE_DATA) ?: return mutableMapOf()
        return JSONObject(jsonStr).toMap()
    }

    companion object {

        private const val KEY_PAGE_NAME = "pageName"
        private const val KEY_PAGE_DATA = "pageData"

        init {
            initKuiklyAdapter()
        }

        private fun initKuiklyAdapter() {
            with(KuiklyRenderAdapterManager) {
                krImageAdapter = KRImageAdapter
                krLogAdapter = KRLogAdapter
                krUncaughtExceptionHandlerAdapter = KRUncaughtExceptionHandlerAdapter
                krFontAdapter = KRFontAdapter
                krColorParseAdapter = KRColorParserAdapter(KRApplication.Companion.application)
                krRouterAdapter = KRRouterAdapter
                krThreadAdapter = KRThreadAdapter()
            }
        }
    }
}
