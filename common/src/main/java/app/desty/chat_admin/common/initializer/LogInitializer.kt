package app.desty.chat_admin.common.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import app.desty.chat_admin.common.BuildConfig
import app.desty.sdk.logcat.LogInitializer
import app.desty.sdk.logcat.Logcat
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.ProcessUtils

class LogInitializer : Initializer<String> {

    /**
     * 初始化日志
     */
    private fun initLog(context: Context) {
        if (BuildConfig.desty_debug) {
            val logConfig = LogInitializer.getLogConfig(context, BuildConfig.desty_debug)
                .addExtraInfo("ApkBuildTime", BuildConfig.buildTimeStr)
                .logSavePath(LogInitializer.getDiskCacheDir(context,ProcessUtils.getCurrentProcessName()))

            Logcat.initialize(context, logConfig)
            CrashUtils.init { crashInfo: CrashUtils.CrashInfo -> Logcat.e(crashInfo.toString()) }
        }
    }

    override fun create(context: Context): String {
        Log.i("Initializer", "LogInitializer:create")
        initLog(context)
        return "LogInitializer"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(ConfigInitializer::class.java)
}