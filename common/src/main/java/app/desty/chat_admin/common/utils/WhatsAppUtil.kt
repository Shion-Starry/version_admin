package app.desty.chat_admin.common.utils

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import app.desty.chat_admin.common.constants.ServerConfig
import app.desty.sdk.logcat.Logcat


object WhatsAppUtil {

    /**
     * 跳转打开desty的WA，不带消息
     */
    fun jumpToDestyWhatsApp(context: Context) {
        jumpToDestyWhatsApp(context, null)
    }

    /**
     * 跳转打开desty的WA，带消息
     * 消息为空时会被过滤
     */
    fun jumpToDestyWhatsApp(context: Context, message: String?) {
        jumpToWhatsApp(
            context,
            ServerConfig.destyWhatsAppNumber,
            message ?: ServerConfig.destyWhatsAppMessage
                      )
    }

    /**
     * 跳转指定号码的WA，不带消息
     * 传入号码需要带国家代号
     */
    fun jumpToWhatsApp(context: Context, phoneNumber: String) {
        jumpToWhatsApp(context, phoneNumber, null)
    }

    /**
     * 跳转指定号码的WA，带消息
     * 消息为空时会被过滤
     * 传入号码需要带国家代号
     */
    private fun jumpToWhatsApp(context: Context, phoneNumber: String, message: String?) {
        val uriBuilder =
            Uri.parse("https://wa.me/${getStandardPhoneNumber(phoneNumber)}").buildUpon()
        if (!TextUtils.isEmpty(message)) {
            uriBuilder.appendQueryParameter("text", message)
        }
        val uri = uriBuilder.build()
        Logcat.i("WA url: $uri")
        OpenIntentUtil.openWeb(uri)
    }

    /**
     * 过滤号码开头的+号和不必要的0
     */
    private fun getStandardPhoneNumber(phoneNumber: String): String {
        if (TextUtils.isEmpty(phoneNumber)) return phoneNumber
        var tmpNumber = phoneNumber
        while (tmpNumber.startsWith("+") || tmpNumber.startsWith("0")) {
            tmpNumber = tmpNumber.substring(1, tmpNumber.length)
        }
        return tmpNumber
    }

}