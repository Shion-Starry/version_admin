package app.desty.chat_admin.home.page

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.common.utils.LoginUtil
import app.desty.chat_admin.common.utils.MyDialog
import app.desty.chat_admin.home.R
import app.desty.chat_admin.home.adapter.HomeNaviAdapter
import app.desty.chat_admin.home.databinding.DrawerHomeNavigationBinding
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.core.DrawerPopupView

class HomeNavigationDrawer(context: Context) : DrawerPopupView(context) {
    var binding: DrawerHomeNavigationBinding? = null
    var clickListener: NaviClickListener? = null
    var homeNaviAdapter: HomeNaviAdapter? = null

    init {
        homeNaviAdapter = createAdapter()
    }

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.apply {
            this.click = ClickEvents()
            this.statusBarHeight = BarUtils.getStatusBarHeight()
            this.adapter = homeNaviAdapter
        }
    }

    override fun getImplLayoutId() = R.layout.drawer_home_navigation

    private fun createAdapter(): HomeNaviAdapter {
        val homeNaviAdapter = HomeNaviAdapter()
        homeNaviAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View, position: Int ->
            val item = adapter.getItem(position)
            if (item is HomePageType) {
                clickListener?.onClick(this@HomeNavigationDrawer, view, item)
            }
        }
        return homeNaviAdapter
    }

    inner class ClickEvents {
        fun clickClose(view: View) {
            dismiss()
        }

        fun clickLogout(view: View) {
            MyDialog.showAdminDialog(ChatAdminDialog.Logout, true, {
                LoginUtil.logout(true)
            })
        }
    }

    interface NaviClickListener {
        fun onClick(
            homeNavigationDrawer: HomeNavigationDrawer,
            view: View,
            pageType: HomePageType
        )
    }
}