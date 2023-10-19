package app.desty.chat_admin.common.bean

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
@Parcelize
data class VersionInfo(
    val channel: String = "",
    @SerialName("compatibleCode") val compatCode: Int = 0,
    @SerialName("compatibleVersion") val compatVersion: String = "",
    val content: String = "",
    val createTime: Long = 0,
    @SerialName("id") val versionId: Int = 0,
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCreateDateTime(): String {
        val instant =Instant.ofEpochMilli(createTime)
        val zoneId = ZoneId.systemDefault()
        val dateTime = ZonedDateTime.ofInstant(instant, zoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(formatter)
    }

}

@Serializable
data class VersionGroup(
    var major: Int = 0,
    var sub: Int = 0,
    var fix: Int = 0,
    var build: Int = 0
) {

    constructor(versionCode: Int): this() {
        major = versionCode / 10000000
        sub = (versionCode / 100000) % 100
        fix = (versionCode / 1000) % 100
        build = versionCode % 1000
    }

    constructor(versionStr: String): this() {
        val versionCode = versionStr.toIntOrNull()
        if (versionCode != null) {
            major = versionCode / 10000000
            sub = (versionCode / 100000) % 100
            fix = (versionCode / 1000) % 100
            build = versionCode % 1000
        } else {
            major = 0
            sub = 0
            fix = 0
            build = 0
        }
    }

    fun getVersionStr(): String {
        return "${major}.${sub}.${fix}"
    }

    fun getVersionCode(): Int {
        return major * 10000000 + sub * 100000 + fix * 1000 + build
    }

    fun getVersionCodeStr(): String {
        return String.format("%d%02d%02d%03d", major, sub, fix, build)
    }

}
