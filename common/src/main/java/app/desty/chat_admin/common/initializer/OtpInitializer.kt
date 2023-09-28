package app.desty.chat_admin.common.initializer

import android.content.Context
import androidx.startup.Initializer
import app.desty.chat.otp.OtpUtils
import app.desty.chat_admin.common.BuildConfig
import app.desty.chat_admin.common.R

class OtpInitializer : Initializer<String> {
    override fun create(context: Context): String {
        OtpUtils.init(
            context,
            R.drawable.ic_default_avatar,
            if (BuildConfig.DEBUG) 1 else 0,
            "~ZHc,Kl:p6[@18-x"
                     )
        return "OtpInitializer"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}