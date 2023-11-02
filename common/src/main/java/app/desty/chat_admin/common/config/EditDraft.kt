package app.desty.chat_admin.common.config

import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.bean.VersionInfo
import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serialLazy


@SerializeConfig(mmapID = "version_draft", cryptKey = "jAk1PJ>h2(v1T))k")
object EditDraft {
    private var verInfoTest: VersionInfo? by serialLazy(null)
    private var verInfoProd: VersionInfo? by serialLazy(null)
    private var cloudConfigInfoTest: CloudConfigInfo? by serialLazy(null)
    private var cloudConfigInfoProd: CloudConfigInfo? by serialLazy(null)

    fun getVerInfoByEnv(env: Environment): VersionInfo? {
        return when (env) {
            Environment.Test    -> verInfoTest
            Environment.Prod   -> verInfoProd
        }
    }

    fun getCloudInfoByEnv(env: Environment): CloudConfigInfo? {
        return when (env) {
            Environment.Test    -> cloudConfigInfoTest
            Environment.Prod   -> cloudConfigInfoProd
        }
    }

    fun setVerInfoByEnv(env: Environment, verInfo: VersionInfo?) {
        when (env) {
            Environment.Test    -> verInfoTest = verInfo
            Environment.Prod   -> verInfoProd = verInfo
        }
    }

    fun setCloudInfoByEnv(env: Environment, configInfo: CloudConfigInfo?) {
        when (env) {
            Environment.Test    -> cloudConfigInfoTest = configInfo
            Environment.Prod   -> cloudConfigInfoProd = configInfo
        }
    }

}