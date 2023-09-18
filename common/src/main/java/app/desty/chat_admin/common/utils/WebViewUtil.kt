package app.desty.chat_admin.common.utils

import android.content.Intent
import app.desty.chat_admin.common.page.WebViewActivity

object WebViewUtil {

    fun openWebView(url: String) {
        app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().topActivity
            .startActivity(
                Intent().apply {
                    setClass(
                        app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().topActivity,
                        WebViewActivity::class.java
                            )
                    putExtra("url",url)
                })
    }
}