package app.desty.chat_admin.common.config

import app.desty.chat_admin.common.bean.UserInfo
import app.desty.sdk.logcat.Logcat
import com.drake.serialize.serialize.annotation.SerializeConfig
import com.drake.serialize.serialize.serial
import com.drake.serialize.serialize.serialLazy
import java.sql.Time

@SerializeConfig(mmapID = "user_config", cryptKey = "~P)*ulzNT9E@4JlJIuPB")
object UserConfig {
    var isLoginStatus: Boolean by serial(false)
    var token: String by serialLazy("")
    var imAccount: String by serialLazy("")
    var userInfo: UserInfo? by serialLazy(null)
    var tokenExpirationTime: Time by serialLazy()

    fun updateUserInfo(userInfo: UserInfo){
        this.userInfo = userInfo
        this.imAccount = userInfo.imAccount
    }

    fun clearAll() {
        Logcat.i("UserConfig:clearAll()")
        isLoginStatus = false
        token = ""
        imAccount = ""
        userInfo = null
    }
}