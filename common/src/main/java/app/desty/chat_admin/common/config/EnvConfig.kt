package app.desty.chat_admin.common.config

import app.desty.chat_admin.common.BuildConfig
import app.desty.chat_admin.common.R
import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serialLazy

@SerializeConfig(mmapID = "env_config", cryptKey = "~P)*ulzNT9E@4JlJIuPB")
object EnvConfig {
    var currentEnvironment: Environment by serialLazy(Environment.Test)

    private fun getEnvironment() =
        if (BuildConfig.desty_debug) currentEnvironment else Environment.Test

    fun getBaseUrl() = when (getEnvironment()) {
//        Environment.Dev     -> "https://platform-gateway-dev.desty.one"
//        Environment.Staging -> "https://platform-gateway-staging.desty.one"
        Environment.Test    -> "https://platform-gateway.desty.one"
//        Environment.Prod    -> "https://pulibic-gateway.desty.app"
        else                -> "https://platform-gateway.desty.one"
    }

    fun getSocketHost() = when (getEnvironment()) {
//        Environment.Dev  -> "47.243.46.46"
        Environment.Test -> "47.243.46.46"
//        Environment.Test -> "192.168.8.163" // 本地环境
//        Environment.Prod -> "147.139.141.200"
        else             -> "8.210.154.116"
    }

    fun getSocketPort() = when (getEnvironment()) {
//        Environment.Dev  -> 28001
        Environment.Test -> 38001
//        Environment.Test -> 28001  // 本地环境
//        Environment.Prod -> 38001
        else             -> 44
    }

    fun getCurrentEnvIconRes() = when (getEnvironment()) {
//        Environment.Dev  -> R.drawable.ic_dev_chat
        Environment.Test -> R.drawable.ic_dev_staging
//        Environment.Prod -> R.drawable.ic_dev_prod
        else             -> R.drawable.ic_dev_custom
    }


}

enum class Environment {
//    Dev,
//    Staging,
    Test,
//    Prod


}