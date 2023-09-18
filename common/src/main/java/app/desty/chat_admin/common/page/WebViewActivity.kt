package app.desty.chat_admin.common.page

import android.os.Bundle
import android.widget.LinearLayout
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import com.just.agentweb.AgentWeb


class WebViewActivity : app.desty.chat_admin.common.base.BaseVmActivity<WebViewVM>() {

    private var mAgentWeb: AgentWeb? = null

    override fun init(savedInstanceState: Bundle?) {
        mState.url.value = intent.getStringExtra("url")
        val findViewById = findViewById<LinearLayout>(R.id.ll_webview)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(findViewById, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(mState.url.value)
        mAgentWeb?.webCreator?.webView?.settings?.javaScriptEnabled = true
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): app.desty.chat_admin.common.base.DataBindingConfig =
        app.desty.chat_admin.common.base.DataBindingConfig(
            R.layout.activity_webview,
            BR.mState,
            mState
                                                          )

    override fun onResume() {
        super.onResume()
        mAgentWeb?.webLifeCycle?.onResume()//恢复
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb?.webLifeCycle?.onPause()//恢复

    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.webLifeCycle?.onDestroy()//恢复
    }
}