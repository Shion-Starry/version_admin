package app.desty.chat_admin.common.handler

import android.os.Handler
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.utils.LoginUtil
import app.desty.chat_admin.common.utils.MyToast

object TokenExpirationHandler {

    private val handler = Handler()
    private val checkExpirationRunnable = Runnable { checkExpirationTime() }

    fun startMonitoring() {
        handler.postDelayed(checkExpirationRunnable, 1000)
    }

    fun stopMonitoring() {
        handler.removeCallbacks(checkExpirationRunnable)
    }

    private fun checkExpirationTime() {
        val expirationTime = UserConfig.tokenExpirationTime
        val currentTime = System.currentTimeMillis()
        if (expirationTime != null && expirationTime < currentTime) {
            handleTokenExpired()
        } else {
            handler.postDelayed(checkExpirationRunnable, 1000)
        }
    }

    private fun handleTokenExpired() {
        MyToast.showToast(R.string.login_expired)
        LoginUtil.logout(true)
    }
}