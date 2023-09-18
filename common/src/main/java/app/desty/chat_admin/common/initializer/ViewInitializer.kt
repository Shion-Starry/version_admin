package app.desty.chat_admin.common.initializer

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.startup.Initializer
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.BuildConfig
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.constants.RouteConstants
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.imuxuan.floatingview.FloatingMagnetView
import com.imuxuan.floatingview.FloatingView
import com.imuxuan.floatingview.MagnetViewListener
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class ViewInitializer : Initializer<String> {
    override fun create(context: Context): String {
        Log.i("Initializer", "ViewInitializer:create")
        initBRV(context)
        initFloatingView()
        XPopup.isLightStatusBar = 1
        XPopup.isLightNavigationBar = 1
        return "ViewInitializer"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(ConfigInitializer::class.java)

    private fun initBRV(context: Context) {
        BRV.modelId = BR.data
        BRV.clickThrottle = 500 // 单位毫秒
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
            setRetryIds(R.id.btn_error_refresh)
            onEmpty {
                if (it is Int) {
                    findViewById<TextView>(R.id.tv_empty).setText(it)
                }
            }
        }
        //"上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_PULLING = StringUtils.getString(R.string.load_more_loading)
        //"释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_RELEASE =
            StringUtils.getString(R.string.load_more_load_failed)
        //"正在加载...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = StringUtils.getString(R.string.load_more_loading)
        //"正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = StringUtils.getString(R.string.load_more_loading)
        //"加载完成";
        ClassicsFooter.REFRESH_FOOTER_FINISH =
            StringUtils.getString(R.string.load_more_load_complete)
        //"加载失败";
        ClassicsFooter.REFRESH_FOOTER_FAILED = StringUtils.getString(R.string.load_more_load_failed)
        //"没有更多数据了";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = StringUtils.getString(R.string.load_more_load_end)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ ->
            MaterialHeader(context)
                .setColorSchemeColors(ColorUtils.getColor(R.color.chat_600))
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ -> ClassicsFooter(context) }
    }

    private fun initFloatingView() {
        if (!BuildConfig.desty_debug) return
        FloatingView.get().icon(EnvConfig.getCurrentEnvIconRes())
        FloatingView.get().customView(R.layout.floating_dev)
        FloatingView.get().add()
        FloatingView.get().listener(object : MagnetViewListener {
            override fun onRemove(magnetView: FloatingMagnetView) {}
            override fun onClick(magnetView: FloatingMagnetView) {
                ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Internal.internal).navigation()
            }
        })
    }
}