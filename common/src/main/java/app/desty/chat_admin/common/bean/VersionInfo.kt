package app.desty.chat_admin.common.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class VersionInfo(
    val versionId: String = "",
    val versionName: String = "",
    val configValue: String = "",
    val latestVersion: Int = 1,
    val compatVersion: Int = 1,
    val channel: String = "android"
    ) : Parcelable
