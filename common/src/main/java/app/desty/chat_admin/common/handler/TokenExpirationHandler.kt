package app.desty.chat_admin.common.handler

import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.utils.LoginUtil
import app.desty.chat_admin.common.utils.MyToast
import com.drake.net.time.Interval
import java.util.concurrent.TimeUnit

object TokenExpirationHandler {

    private var interval: Interval? = null
//    private val handler = Handler(Looper.getMainLooper())
//    private val checkExpirationRunnable = Runnable { checkExpirationTime() }

    fun startMonitoring() {
//        hander.cancel()
//        handler.postDelayed(checkExpirationRunnable, 1000)
        if (interval == null) {
            checkExpirationTime()
        }
    }

    fun stopMonitoring() {
//        handler.removeCallbacks(checkExpirationRunnable)
        interval?.cancel()
        interval = null
    }

    private fun checkExpirationTime() {
        val currentTime = System.currentTimeMillis()
        val expirationTime = UserConfig.tokenExpirationTime ?: currentTime
        interval = Interval(expirationTime - currentTime, 1000, TimeUnit.MILLISECONDS)
            .finish {
                handleTokenExpired()
            }.start()
    }

    private fun handleTokenExpired() {
        MyToast.showToast(R.string.login_expired)
        LoginUtil.logout(true)
    }
}