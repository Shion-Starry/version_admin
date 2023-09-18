package app.desty.chat_admin.common.enum_bean

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import app.desty.chat_admin.common.R

enum class ChatNotificationEnum (
    val apiType: String = "",
    val channelName: String = "",
    val musicUri: Int = 0,
    val needChannel: Boolean = false,
    val coverToNotifyWhenBackground: Boolean = false,
    val priority:Int= NotificationManager.IMPORTANCE_HIGH,
                             ) {
    ImMessage("CHAT_IM_MESSAGE",channelName="Message", musicUri = R.raw.new_message, needChannel = true),
    SyncProgress("SYNC_PROGRESS", channelName = "Progress", needChannel = true, priority = NotificationManager.IMPORTANCE_DEFAULT),
    ;

    fun getMusicUri(context: Context): Uri =
        Uri.parse("android.resource://" + context.packageName + "/" + musicUri)


    companion object {
        private val map: MutableMap<String, ChatNotificationEnum> by lazy {
            mutableMapOf<String, ChatNotificationEnum>().apply {
                values().forEach {
                    //防老六，有时候返回大写，有时候返回小写
                    this += it.apiType.lowercase() to it
                }
            }
        }

        fun getEnumByCode(code: String?): ChatNotificationEnum? {
            if (TextUtils.isEmpty(code)) return null
            return try {
                map[code!!.lowercase()]
            } catch (e: Exception) {
                null
            }
        }
    }
}