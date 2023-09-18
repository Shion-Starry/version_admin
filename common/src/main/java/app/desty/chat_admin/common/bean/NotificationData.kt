package app.desty.chat_admin.common.bean

import app.desty.chat_admin.common.constants.DestyConstants
import app.desty.chat_admin.common.enum_bean.ChatNotificationEnum

data class NotificationData(
    val action:String = DestyConstants.notificationAction,
    val channel: ChatNotificationEnum? = null,
    val notificationId: Int = 0,
    val gtMsgId: String? = null,
    val gtTaskId: String? = null,
    val data: Any? = null,
    val ttl: Long = 0,
    val progress :Int = -1,
                           )
