package app.desty.chat_admin.common.bean

import android.os.Parcelable
import app.desty.chat_admin.common.enum_bean.ServerConfigNameEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class Config(
    val name: ServerConfigNameEnum?,//配置名字枚举
    val value: String?,//配置值
                 ) : Parcelable