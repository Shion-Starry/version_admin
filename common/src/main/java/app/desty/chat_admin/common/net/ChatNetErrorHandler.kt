package app.desty.chat_admin.common.net

import android.view.View
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.utils.LoginUtil
import app.desty.chat_admin.common.utils.MyToast
import app.desty.sdk.logcat.Logcat
import com.blankj.utilcode.util.StringUtils
import com.drake.net.Net
import com.drake.net.exception.ConvertException
import com.drake.net.exception.DownloadFileException
import com.drake.net.exception.HttpFailureException
import com.drake.net.exception.NetConnectException
import com.drake.net.exception.NetException
import com.drake.net.exception.NetSocketTimeoutException
import com.drake.net.exception.NoCacheException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.drake.net.exception.URLParseException
import com.drake.net.interfaces.NetErrorHandler
import java.net.UnknownHostException

object ChatNetErrorHandler : NetErrorHandler {
    override fun onError(e: Throwable) {
        val finalMessage = getMessageByError(e)
        MyToast.showToast(finalMessage)

        Net.debug(e)
        Logcat.e().msg(finalMessage).msg(e).out()
    }

    override fun onStateError(e: Throwable, view: View) {
        super.onStateError(e, view)
    }

    fun getMessageByError(e: Throwable): String {
        var error = e
        if (e is ConvertException && e.cause != null) {
            //由于在转化器里处理业务错误逻辑，所以可能会包含业务错误cause
            //若包含业务错误，则后续以业务错误为主
            error = e.cause!!
        }
        val message: Any? = when (error) {
            is UnknownHostException      -> R.string.chat_net_host_error
            is URLParseException         -> R.string.chat_net_url_error
            is NetConnectException       -> R.string.chat_net_connect_error
            is NetSocketTimeoutException -> StringUtils.getString(
                R.string.chat_net_connect_timeout_error,
                error.message
                                                                 )

            is DownloadFileException     -> R.string.chat_net_download_error
            is ConvertException          -> R.string.chat_net_parse_error
            is RequestParamsException    -> R.string.chat_net_request_error
            is ServerResponseException   -> R.string.chat_net_server_error
            is NullPointerException      -> R.string.chat_net_null_error
            is NoCacheException          -> R.string.chat_net_no_cache_error
            is ResponseException         -> error.message
            is HttpFailureException      -> R.string.chat_net_request_failure
            is NetException              -> R.string.chat_net_unknown_net_error
            is TokenException            -> {
                LoginUtil.logout()
                "${error.msg} [${error.code}]"
            }

//            is ForceUpgradeException     -> {
//                error.body?.apply {
//                    val upgradeBean =
//                        SerializationConverter.jsonDecoder.decodeFromString<UpgradeBean>(this)
//                    UpgradeDialog.showUpgradeDialog(
//                        upgradeBean = upgradeBean,
//                        isForceUpgrade = true
//                                                   )
//                }
//                ""
//            }

            is ImTokenNotExistsException -> null // 后台静默接口
            is BaseNetThrowable          -> "${error.msg} [${error.code}]"
            else                         -> R.string.chat_net_unknown_error
        }
        return when (message) {
            is Int    -> StringUtils.getString(message)
            is String -> message
            else      -> ""
        }
    }
}