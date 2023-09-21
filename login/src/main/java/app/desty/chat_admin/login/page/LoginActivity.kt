package app.desty.chat_admin.login.page

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import app.desty.chat_admin.login.BR
import app.desty.chat_admin.login.R
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.handler.TokenExpirationHandler
import app.desty.chat_admin.common.utils.LoginUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = RouteConstants.Login.login)
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    private val pageList = listOf(LoginPageType.LoginAdmin)
    private lateinit var tokenExpirationHandler: TokenExpirationHandler

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initViewModel() {
        mState.pageType.observe(this, this::switchFragment)

        tokenExpirationHandler = TokenExpirationHandler {
            LoginUtil.logout(true)
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.activity_login, BR.mState, mState)

    override fun onResume() {
        super.onResume()
        tokenExpirationHandler.startMonitoring()
    }

    override fun onPause() {
        super.onPause()
        tokenExpirationHandler.stopMonitoring()
    }

    private fun switchFragment(pageType: LoginPageType) {
        val fragmentName: String = pageType.fragmentPath
        val fragmentManager = supportFragmentManager

        var fragment = fragmentManager.findFragmentByTag(fragmentName)
        if (fragment != null && fragment.isVisible) {
            return
        }
        val ft = fragmentManager.beginTransaction()
        for (enumPageType in pageList) {
            val currentFragmentType: String = enumPageType.fragmentPath
            if (TextUtils.equals(fragmentName, currentFragmentType)) {
                fragment = fragmentManager.findFragmentByTag(currentFragmentType)
                if (fragment == null) {
                    fragment = ARouter.getInstance().build(currentFragmentType).navigation() as Fragment?
                    if (fragment == null) {
                        return
                    }
                    ft.add(R.id.fragment_container, fragment, fragmentName)
                } else {
                    if (fragment.isHidden) {
                        ft.show(fragment)
                    }
                }
            } else {
                fragment = fragmentManager.findFragmentByTag(currentFragmentType)
                if (fragment != null && fragment.isVisible) {
                    ft.hide(fragment)
                }
            }
        }
        ft.commitAllowingStateLoss()
        fragmentManager.executePendingTransactions()
    }
}
