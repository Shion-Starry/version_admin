package app.desty.chat_admin.login.page

import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.constants.RouteConstants

class LoginViewModel : BaseVM() {
    val pageType = MutableLiveData(LoginPageType.LoginAdmin)
}

enum class LoginPageType(val fragmentPath: String) {
    LoginAdmin(RouteConstants.Login.loginAdmin)
}