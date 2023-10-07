package app.desty.chat_admin.home.page

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.common.handler.TokenExpirationHandler
import app.desty.chat_admin.home.BR
import app.desty.chat_admin.home.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition

@Route(path = RouteConstants.Home.homePage)
class HomeActivity : BaseVmActivity<HomeViewModel>() {
    private var basePopupView: BasePopupView? = null
    private var homeNavigationDrawer: HomeNavigationDrawer? = null
    private val naviModuleList = mutableListOf<HomePageType>().apply {
        HomePageType.values().forEach {
            add(it)
        }
    }
    private val defaultPageType = HomePageType.Home
    private var nowPageType: HomePageType? = null
    private val menuClick = View.OnClickListener { _: View? ->
        if (basePopupView != null) {
            basePopupView?.show()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initNaviList()
        initDrawer()
    }

    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.activity_home, BR.mState, mState)

    override fun onResume() {
        super.onResume()
        TokenExpirationHandler.startMonitoring()
    }

    override fun onPause() {
        super.onPause()
        TokenExpirationHandler.stopMonitoring()
    }

    override fun getToolbarConfig(): ToolbarConfig {
        val tmpType = nowPageType ?: defaultPageType
        return ToolbarConfig(
            titleTextBold = true,
            title = getString(tmpType.titleStringRes),
            showMenu = true,
            menuClick = menuClick
        )
    }

    private fun initNaviList() {
        naviModuleList.clear()
        for (pageType in HomePageType.values()) {
            if (TextUtils.isEmpty(pageType.fragmentPath)) continue
            naviModuleList.add(pageType)
        }
    }

    private fun initDrawer() {
        if (homeNavigationDrawer != null) return
        homeNavigationDrawer = HomeNavigationDrawer(this)
        homeNavigationDrawer?.homeNaviAdapter?.setList(naviModuleList)
        homeNavigationDrawer?.clickListener = (object : HomeNavigationDrawer.NaviClickListener {
            override fun onClick(
                homeNavigationDrawer: HomeNavigationDrawer,
                view: View,
                pageType: HomePageType
            ) {
                homeNavigationDrawer.dismiss()
                switchFragment(pageType)
            }
        })

        basePopupView = XPopup.Builder(this)
            .popupPosition(PopupPosition.Left)
            .hasStatusBarShadow(false)
            .isLightStatusBar(false)
            .hasNavigationBar(true)
            .isLightNavigationBar(true)
            .navigationBarColor(Color.WHITE)
            .asCustom(homeNavigationDrawer)
    }

    private fun switchFragment(pageType: HomePageType?) {
        var thePageType: HomePageType = pageType ?: return
        if (!naviModuleList.contains(thePageType)) thePageType = defaultPageType
        nowPageType = thePageType
        val fragmentName: String = thePageType.fragmentPath
        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(fragmentName)
        if (fragment != null && fragment.isVisible) {
            return
        }

        val ft = fragmentManager.beginTransaction()
        for (enumPageType in naviModuleList) {
            val currentFragment: String = enumPageType.fragmentPath
            if (TextUtils.equals(fragmentName, currentFragment)) {
                fragment = fragmentManager.findFragmentByTag(currentFragment)
                if (fragment == null) {
                    fragment = ARouter.getInstance().build(currentFragment).navigation() as Fragment?
                    if (fragment == null) return
                    ft.add(R.id.fragment_container, fragment, fragmentName)
                } else {
                    if (fragment.isHidden) {
                        ft.show(fragment)
                    }
                }
            } else {
                fragment = fragmentManager.findFragmentByTag(currentFragment)
                if (fragment != null && fragment.isVisible) {
                    ft.hide(fragment)
                }
            }
        }
        buildToolbar(thePageType)
        ft.commitAllowingStateLoss()
        fragmentManager.executePendingTransactions()
    }

    private fun buildToolbar(pageType: HomePageType) {
        val value = mState.toolbarConfig
        if (value != null) {
            value.title = pageType.titleStringRes.toString()
            if (pageType.rightIconRes != 0) {
                value.showRightOperateIcon = true
                value.rightOperateIconResId = pageType.rightIconRes
            } else {
                value.showRightOperateIcon = false
            }
            mState.toolbarConfig = value
        }
    }

}