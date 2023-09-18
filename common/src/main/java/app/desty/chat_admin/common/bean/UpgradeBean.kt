package app.desty.chat_admin.common.bean

import com.blankj.utilcode.util.AppUtils
import kotlinx.serialization.Serializable

@Serializable
data class UpgradeBean(
    val channel: String = "",
    val compatibleCode: Int = 0,
    val compatibleVersion: String = "",
    val content: String = "",
    val createTime: Long = 0,
    val id: Int = 0,
    val latestCode: Int = 0,
    val latestVersion: String = "",
    val marketUrl: String = "",
    val state: Int = 0,
    val updateTime: Long = 0,
    val url: String = "",
    val websiteUrl: String = ""
                      ) {
    fun getShowContent(): String = content.ifEmpty { "default" }

    fun hasUpgrade(): Boolean = latestCode > AppUtils.getAppVersionCode()

    fun isForceUpgrade(): Boolean = compatibleCode > AppUtils.getAppVersionCode()
}