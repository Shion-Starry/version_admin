package app.desty.chat_admin.common.bean

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
@Parcelize
data class VersionInfo(
    val channel: String = "",
    val compatCode: Int = 0,
    val compatVersion: String = "",
    val content: String = "",
    val createTime: Long = 0,
    val versionId: Int = 0,
    val latestCode: Int = 0,
    val latestVersion: String = "",
    val marketUrl: String = "",
    val state: Int = 0,
    val updateTime: Long = 0,
    val url: String = "",
    val websiteUrl: String = ""
    ) : Parcelable {

        @RequiresApi(Build.VERSION_CODES.O)
        fun getUpdateDateTime(): String {
            val instant = Instant.ofEpochMilli(updateTime)
            val zoneId = ZoneId.systemDefault()
            val dateTime = ZonedDateTime.ofInstant(instant, zoneId)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return dateTime.format(formatter)
        }

    }
