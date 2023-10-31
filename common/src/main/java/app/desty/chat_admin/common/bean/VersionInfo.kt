package app.desty.chat_admin.common.bean

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
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
    @SerialName("compatibleCode")
    @SerializedName("compatibleCode")
    val compatCode: Int = 0,
    @SerialName("compatibleVersion")
    @SerializedName("compatibleVersion")
    val compatVersion: String = "",
    var content: String = "",
    @Transient
    val createTime: Long = 0,
    @Transient
    @SerialName("id")
    @SerializedName("id")
    val versionId: Int = 0,
    val latestCode: Int = 0,
    val latestVersion: String = "",
    val marketUrl: String = "",
    @Transient
    val state: Int = 0,
    @Transient
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

    override fun equals(other: Any?): Boolean {
        return if (other is VersionInfo) {
            (channel == other.channel
                    && latestVersion == other.latestVersion
                    && latestCode == other.latestCode
                    && compatVersion == other.compatVersion
                    && compatCode == other.compatCode
                    && url == other.url
                    && websiteUrl == other.websiteUrl
                    && marketUrl == other.marketUrl
                    && content == other.content)
        } else {
            false
        }
    }

}

@Serializable
data class VersionGroup(
    var major: Int = 0,
    var sub: Int = 0,
    var fix: Int = 0,
    var build: Int = 0
) {

    constructor(versionCode: Int):this() {
        if (versionCode > 0) {
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

    fun getVersionCodeStr(): String {
        return String.format("%d%02d%02d%03d", major, sub, fix, build)
    }

}
