package app.desty.chat_admin.common.initializer

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import app.desty.chat_admin.common.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

class ARouterInitializer : Initializer<String> {
    override fun create(context: Context): String {
        Log.i("Initializer", "RouterInitializer:create")
        if (BuildConfig.desty_debug) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        ARouter.init(context.applicationContext as Application)
        return "ARouterInit"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(ConfigInitializer::class.java)
}