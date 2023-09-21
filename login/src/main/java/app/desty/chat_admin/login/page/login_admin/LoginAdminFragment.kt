package app.desty.chat_admin.login.page.login_admin

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.login.R
import app.desty.chat_admin.login.BR
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.net.utils.scopeDialog

@Route(path = RouteConstants.Login.loginAdmin)
class LoginAdminFragment : BaseVMFragment<LoginAdminViewModel>() {

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_login_admin, BR.mState, mState)
                    .addBindingParam(BR.click, ClickEvents())
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    inner class ClickEvents {

        fun clickLogin(view: View) {
            scopeDialog (block = mState.requestLogin())
        }

        fun clickShowPassword(view: View) {
            mState.isShowPassword.value = !(mState.isShowPassword.value ?: false)
        }
    }
}