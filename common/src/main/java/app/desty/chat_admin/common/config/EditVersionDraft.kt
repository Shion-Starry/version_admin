package app.desty.chat_admin.common.config

import app.desty.chat_admin.common.bean.VersionInfo
import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serialLazy


@SerializeConfig(mmapID = "version_draft", cryptKey = "jAk1PJ>h2(v1T))k")
object EditVersionDraft {
    var verInfo: VersionInfo? by serialLazy(null)


}