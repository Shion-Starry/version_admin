package app.desty.chat_admin.common.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import app.desty.chat_admin.common.config.AppConfig
import app.desty.chat_admin.common.config.JsonSerializeHook
import com.drake.serialize.serialize.Serialize
import com.tencent.mmkv.MMKV

class ConfigInitializer : Initializer<String> {

    private fun initConfig(context: Context) {
        // 可选的初始化配置
        MMKV.initialize(context)
        // MMKV.initialize(cacheDir.absolutePath, MMKVLogLevel.LevelInfo) // 参数1: 存储路径, 参数2: 日志等级
        Serialize.hook = JsonSerializeHook()
        AppConfig.init()
    }

    override fun create(context: Context): String {
        Log.i("Initializer", "ConfigInitializer:create")
        initConfig(context)
        return "ConfigInitializer"
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}