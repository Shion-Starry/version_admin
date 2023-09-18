package app.desty.chat_admin.welcome

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.BR
import app.desty.chat_admin.R
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = RouteConstants.Main.welcome)
class WelcomeActivity : BaseVmActivity<WelcomeVM>() {
    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(
            R.layout.activity_welcome,
            BR.mState,
            mState
                                                          )
            .addBindingParam(BR.click, ClickProxy())

    inner class ClickProxy {
        fun clickLogin(v: View) {
            ARouter.getInstance().build(RouteConstants.Login.login).navigation()
        }
    }

}