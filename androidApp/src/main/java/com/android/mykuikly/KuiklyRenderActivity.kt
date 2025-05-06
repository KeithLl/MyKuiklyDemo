package com.android.mykuikly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.tencent.kuikly.core.render.android.IKuiklyRenderExport
import com.tencent.kuikly.core.render.android.adapter.KuiklyRenderAdapterManager
import com.tencent.kuikly.core.render.android.css.ktx.toMap
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegatorDelegate
import com.tencent.kuikly.core.render.android.expand.KuiklyRenderViewBaseDelegator
import com.android.mykuikly.adapter.KRColorParserAdapter
import com.android.mykuikly.adapter.KRFontAdapter
import com.android.mykuikly.adapter.KRImageAdapter
import com.android.mykuikly.adapter.KRLogAdapter
import com.android.mykuikly.adapter.KRRouterAdapter
import com.android.mykuikly.adapter.KRThreadAdapter
import com.android.mykuikly.adapter.KRUncaughtExceptionHandlerAdapter
import com.android.mykuikly.module.KRBridgeModule
import com.android.mykuikly.module.KRMyLogModule
import com.android.mykuikly.module.KRShareModule
import com.android.mykuikly.widgets.HRMyImageView
import com.tencent.kuikly.core.render.android.expand.module.getKuiklyEventName
import com.tencent.kuikly.core.render.android.expand.module.getKuiklyEventParams
import com.tencent.kuikly.core.render.android.expand.module.registerKuiklyBroadcastReceiver
import com.tencent.kuikly.core.render.android.expand.module.unregisterKuiklyBroadcastReceiver
import org.json.JSONObject

class KuiklyRenderActivity : AppCompatActivity(), KuiklyRenderViewBaseDelegatorDelegate {

    private lateinit var hrContainerView: ViewGroup
    private lateinit var loadingView: View
    private lateinit var errorView: View

    private val kuiklyRenderViewDelegator = KuiklyRenderViewBaseDelegator(this)

    private val pageName: String
        get() {
            val pn = intent.getStringExtra(KEY_PAGE_NAME) ?: ""
            return if (pn.isNotEmpty()) {
                return pn
            } else {
                "router"
            }
        }


    val kuiklyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val eventName = intent.getKuiklyEventName() // 接收到的事件名字
            val data = intent.getKuiklyEventParams() // kuikly侧传递的参数
            Log.e(
                "Keith",
                "Receive event in RenderActivity , pageName is : ${pageName}: $eventName, data: $data"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hr)
        setupImmersiveMode()
        hrContainerView = findViewById(R.id.hr_container)
        loadingView = findViewById(R.id.hr_loading)
        errorView = findViewById(R.id.hr_error)
        kuiklyRenderViewDelegator.onAttach(hrContainerView, "", pageName, createPageData())
        registerKuiklyBroadcastReceiver(kuiklyReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        kuiklyRenderViewDelegator.onDetach()
        unregisterKuiklyBroadcastReceiver(kuiklyReceiver)
    }

    override fun onPause() {
        super.onPause()
        kuiklyRenderViewDelegator.onPause()
    }

    override fun onResume() {
        super.onResume()
        kuiklyRenderViewDelegator.onResume()

        // 发送事件到Kuikly
//        Handler().postDelayed({
//            sendKuiklyEvent("test", JSONObject().apply{
//                put("test", "TestMsg from MainApp")
//            })
//        }, 2500)
    }

    override fun registerExternalModule(kuiklyRenderExport: IKuiklyRenderExport) {
        super.registerExternalModule(kuiklyRenderExport)
        // 注册模块
        Log.e("Keith", "Register Module $kuiklyRenderExport")
        with(kuiklyRenderExport) {
            moduleExport(KRBridgeModule.MODULE_NAME) {
                KRBridgeModule()
            }
            moduleExport(KRShareModule.MODULE_NAME) {
                KRShareModule()
            }
            Log.e("Keith", "Register Module inner ${KRMyLogModule.MODULE_NAME}")
            moduleExport(KRMyLogModule.MODULE_NAME) {
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
        val jsonStr = intent.getStringExtra(KEY_PAGE_DATA) ?: return mutableMapOf()
        return JSONObject(jsonStr).toMap()
    }

    private fun setupImmersiveMode() {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = Color.TRANSPARENT
            window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

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
