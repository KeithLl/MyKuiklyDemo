package com.android.mykuikly.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target;
import com.tencent.kuikly.core.render.android.expand.component.KRImageView.Companion.PROP_SRC
import com.tencent.kuikly.core.render.android.export.IKuiklyRenderViewExport
import com.tencent.kuikly.core.render.android.export.KuiklyRenderCallback

/**
 * Created by KeithLee on 2025/4/30.
 * Introduction:自定义View
 */
open class HRMyImageView(context: Context) :
// 继承并实现 IKuiklyRenderViewExport 接口
    AppCompatImageView(context), IKuiklyRenderViewExport {

    private var src = ""
    private var loadSuccessCallback: KuiklyRenderCallback? = null

    override fun setProp(propKey: String, propValue: Any): Boolean {
        // 对应Kuikly侧的属性值,会走到这个方法
        return when (propKey) {
            "src" -> {
                setSrc(propValue as String)
                true
            }
            "loadSuccess" -> {
                loadSuccessCallback = propValue as KuiklyRenderCallback
                true
            }
            else -> super.setProp(propKey, propValue)
        }
    }

    override fun call(method: String, params: String?, callback: KuiklyRenderCallback?): Any? {
        return super.call(method, params, callback)
    }

    private fun setSrc(url: String) {
        if (src == url) {
            return
        }
        src = url
        setImageDrawable(null)
        // 加载并设置图片
        val creator = Picasso.get().load(src)
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                setImageDrawable(BitmapDrawable(bitmap))
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                setImageDrawable(null)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }
        }
        creator.into(target)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        // 调用并通知Kuikly成功
        loadSuccessCallback?.invoke(mapOf(
            PROP_SRC to src
        ))
    }
}
