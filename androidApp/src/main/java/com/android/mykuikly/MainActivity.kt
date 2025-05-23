package com.android.mykuikly

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.mykuikly.pages.medal.MedalListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var flContainer: FrameLayout
    private lateinit var tvJump: TextView

    companion object {

        private const val KEY_PAGE_NAME = "pageName"
        private const val KEY_PAGE_DATA = "pageData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupImmersiveMode()
        flContainer = findViewById(R.id.main_container)
        tvJump = findViewById(R.id.tv_jump_custom)

        tvJump.setOnClickListener {
            Log.e("Keith", "click jump")
            var fragment = MedalListFragment()
            var bundle = Bundle()
            bundle.putString(KEY_PAGE_NAME, "MedalList")
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.main_container, fragment).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupImmersiveMode() {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = Color.TRANSPARENT
            window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    fun openNewPage() {
        Log.e("Keith", "open new page invoke")
    }
}
