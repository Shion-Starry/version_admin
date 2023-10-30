package app.desty.chat_admin.common.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CloudConfigInfo(
    val name: String = "",
    val value: String = "",
    val fromVersionCode: Int = 0,
    val toVersionCode: Int = 0,
    val uniqueKey: String = "",
    val channel: String = ""
) : Parcelable
