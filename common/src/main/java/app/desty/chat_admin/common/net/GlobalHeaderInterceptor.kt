package app.desty.chat_admin.common.net

import app.desty.chat_admin.common.config.AppConfig
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.utils.DeviceUtil
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.request.BaseRequest


/** 演示添加全局请求头/参数 */
class GlobalHeaderInterceptor(
    private val userAgent: String,
    private val versionName: String,
    private val versionCode: Int,
                             ) : RequestInterceptor {

    /** 本方法每次请求发起都会调用, 这里添加的参数可以是动态参数 */
    override fun interceptor(request: BaseRequest) {
        request.setHeader("app-channel", "android")
        request.setHeader("app-version-name", "99.99.99")
        request.setHeader("app-version", "999999999")
        request.setHeader("App", "OmniChat")
        if (UserConfig.token.isNotEmpty() && request.headers().get("Token").isNullOrEmpty()) {
            request.setHeader("Token", UserConfig.token)
        }
        request.setHeader("device-id", DeviceUtil.getDeviceId())
        request.removeHeader("User-Agent")
        request.setHeader("User-Agent", userAgent)
        request.setHeader("language", AppConfig.currentLanguage.apiType)
    }
}