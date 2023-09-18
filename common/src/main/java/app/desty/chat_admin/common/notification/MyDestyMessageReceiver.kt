package app.desty.chat_admin.common.notification

import android.content.Context
import app.desty.chat_admin.common.utils.PushingBindUtil
import app.desty.sdk.logcat.Logcat
import app.desty.sdk_push.util.DestyPushReceiver

object MyDestyMessageReceiver : DestyPushReceiver {
    override fun onPushConnectStatus(context: Context?, online: Boolean) {
    }

    override fun onPushClientIdChanged(context: Context?, clientId: String?) {
        Logcat.i("onPushClientIdChanged:$clientId")
        PushingBindUtil.autoBindOrUnbind(clientId)
    }

    override fun onPushMessage(context: Context?, data: MutableMap<String, Any>?) {
    }

    override fun onNotificationMessageArrived(context: Context?, data: MutableMap<String, Any>?) {
    }

    override fun onNotificationMessageClicked(context: Context?, data: MutableMap<String, Any>?) {
    }
}