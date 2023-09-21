package app.desty.chat_admin.common.handler

import android.os.Handler
import app.desty.chat_admin.common.config.UserConfig

class TokenExpirationHandler(private val onTokenExpired: () -> Unit) {

    private val handler = Handler()
    private val checkExpirationRunnable = Runnable { checkExpirationTime() }

    fun startMonitoring() {
        handler.postDelayed(checkExpirationRunnable, 1000)
    }

    fun stopMonitoring() {
        handler.removeCallbacks(checkExpirationRunnable)
    }

    private fun checkExpirationTime() {
        val currentTime = System.currentTimeMillis()
        val expirationTime = UserConfig.tokenExpirationTime
        if (expirationTime != null && expirationTime < currentTime) {
            onTokenExpired.invoke()
        } else {
            handler.postDelayed(checkExpirationRunnable, 1000)
        }
    }
}