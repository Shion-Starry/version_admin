package app.desty.chat_admin.login.page.login_admin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.LoginAdminToken
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.utils.LoginUtil
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.login.R
import app.desty.chat_admin.login.api.LoginApi
import com.blankj.utilcode.util.RegexUtils
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class LoginAdminViewModel : BaseVM() {

    // Why does naming 'ifShowPassword' result in a different calling method?
    // 'mState.isShowPassword()' --> 'mState.ifShowPassword'

    val isShowPassword = MutableLiveData(false)
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    fun requestLogin(): suspend CoroutineScope.() -> Unit = {
        if (checkInputFormat()) {
            val loginToken = Post<LoginAdminToken>(LoginApi.loginAdmin) {
                json(
                    "account" to (username.value?.trim()?: ""),
                    "password" to password.value
                )
            }.await()
            if (loginToken.token.isNotEmpty()) {
                loginSuccessfully(loginToken.token, loginToken.expirationTime)
            }
        }
    }

    private fun checkInputFormat(): Boolean {
        val account = username.value
        val psw = password.value
        val toastText = when {
            account.isNullOrBlank()              -> R.string.login_check_username_empty
            !RegexUtils.isUsername(account)       -> R.string.login_check_username_invalid
            psw.isNullOrBlank()             -> R.string.login_check_psw_empty
            else                                  -> 0
        }
        if (toastText != 0) {
            MyToast.showToast(toastText)
            return false
        }
        return true
    }

    /**
     * 这个方法在请求登录成功后，用户设置中的token更新为
     * 当前输入的token，并且将用户设置中的登录状态设置为
     * true。随后根据当前输入的token向后端请求用户信息。
     *
     * @param token - 登录请求成功后从后端得到的令牌
     *
     */
    private fun loginSuccessfully(token: String, expirationTime: Int) {
        LoginUtil.login()
        Log.d("Check Login Status", "Login Request is successfully responded.")
        UserConfig.token = token
        UserConfig.isLoginStatus = true
        checkCurrentDateTime(expirationTime)
        LoginUtil.routeToHomePage()
    }

    private fun checkCurrentDateTime(expirationTime: Int) {
        val currentTime = System.currentTimeMillis()
        val tokenExpirationTime = currentTime + expirationTime
        UserConfig.tokenExpirationTime = tokenExpirationTime
        Log.d("Current time", "The current time is：$currentTime")
        Log.d("Expiration time", "The expiration time of the token is：$tokenExpirationTime")
    }
}