package app.desty.chat_admin.common.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.HomeRouteBean
import app.desty.chat_admin.common.bean.NotificationData
import app.desty.chat_admin.common.bean.PushSessionBean
import app.desty.chat_admin.common.constants.DestyConstants
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.ChatNotificationEnum
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.Utils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object NotificationUtil {
    private val jsonDecoder = Json {
        isLenient = true
        ignoreUnknownKeys = true // 数据类可以不用声明Json的所有字段
        coerceInputValues = true // 如果Json字段是Null则使用数据类字段默认值
    }
    const val NOTIFY_KEY_PAYLOAD = "payload"
    const val NOTIFY_KEY_CHANNEL_ID = "channelId"
    const val NOTIFY_KEY_GT_MESSAGE_ID = "gtMessageId"
    const val NOTIFY_KEY_GT_TASK_ID = "gtTaskId"
    const val NOTIFY_KEY_NOTIFY_ID = "notifyId"

    private var notificationId = System.currentTimeMillis().toInt() / 1000


    @Synchronized
    private fun getNotificationId(): Int {
        notificationId++
        return notificationId
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getNotificationPriorityByO(priority: Int): Int {
        val priorityOfNotificationManager: Int =
            when (priority) {
                NotificationCompat.PRIORITY_LOW     -> NotificationManager.IMPORTANCE_LOW
                NotificationCompat.PRIORITY_MIN     -> NotificationManager.IMPORTANCE_MIN
                NotificationCompat.PRIORITY_HIGH    -> NotificationManager.IMPORTANCE_HIGH
                NotificationCompat.PRIORITY_DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                else                                -> NotificationManager.IMPORTANCE_NONE
            }
        return priorityOfNotificationManager
    }

    fun showNotification(
        context: Context,
        title: String?,
        content: String?,
        data: NotificationData?,
        priority: Int = NotificationCompat.PRIORITY_MAX
                        ) {
        showNotification(
            context,
            title = title,
            content = content,
            idIco = R.drawable.ic_push_logo,
            iconColor = ColorUtils.getColor(R.color.chat_600),
            autoCancel = true,
            ongoing = false,
            priority = priority,
            data = data
                        )
    }


    /**
     * 开始通知
     *
     * @param context    页面上下文
     * @param title      通知栏标题
     * @param content    通知栏内容
     * @param idIco      通知栏图片id
     * @param autoCancel 通知栏是否可点击消除
     * @param ongoing    通知栏是否可清除
     * @param data       参数
     */
    fun showNotification(
        context: Context,
        title: String?,
        content: String?,
        idIco: Int,
        iconColor: Int,
        autoCancel: Boolean,
        ongoing: Boolean,
        priority: Int,
        data: NotificationData?,
                        ) {
        val applicationContext = context.applicationContext
        val vibrate = longArrayOf(0, 500, 1000, 1500)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification.Builder
        val notificationId =
            if (data != null && data.notificationId != 0) data.notificationId else getNotificationId()
        if (data != null) {
            val intent = setNotificationIntent(notificationId, data, applicationContext)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                                         )
            } else {
                PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                                         )
            }
            // 此处必须兼容android O设备，否则系统版本在O以上可能不展示通知栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = notificationBuilderByO(
                    context,
                    title,
                    content,
                    idIco,
                    iconColor,
                    autoCancel,
                    ongoing,
                    applicationContext,
                    vibrate,
                    pendingIntent,
                    manager,
                    data,
                    priority
                                                     )
            } else {
                notification = Notification.Builder(applicationContext)
                    .setAutoCancel(autoCancel)
                    .setOngoing(ongoing)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setColor(iconColor) //                        .setLargeIcon(BitmapFactory.decodeResource(ApplicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setPriority(priority)
                    .setDefaults(Notification.DEFAULT_ALL) //使用默认的声音、振动、闪光

                if (data.channel?.musicUri?.compareTo(0) != 0) {
                    notification.setSound(data.channel?.getMusicUri(context))
                }
            }
            if (data.progress >= 0) {
                notification.setProgress(100, data.progress, false)
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                notification.setGroup(data.getChannel().getNotifyGroupEnum().getGroupId())
//                    .setGroupSummary(true)
//            }
            manager.notify(notificationId, notification.build())
        }
    }


    /**
     * 设置Intent
     *
     * @param notificationCode
     * @param data
     * @param applicationContext
     * @return
     */
    private fun setNotificationIntent(
        notificationCode: Int,
        data: NotificationData?,
        applicationContext: Context,
                                     ): Intent {
        val postcard: Postcard = ARouter.getInstance().build(app.desty.chat_admin.common.constants.RouteConstants.Main.splash)
        LogisticsCenter.completion(postcard)
        val intent = Intent(applicationContext, postcard.destination)
        intent.action = data?.action
        if (data?.data is String) {
            intent.putExtra(NOTIFY_KEY_PAYLOAD, data.data)
        }
        intent.putExtra(NOTIFY_KEY_CHANNEL_ID, data?.channel?.apiType)
        intent.putExtra(NOTIFY_KEY_GT_MESSAGE_ID, data?.gtMsgId)
        intent.putExtra(NOTIFY_KEY_GT_TASK_ID, data?.gtTaskId)
        intent.putExtra(NOTIFY_KEY_NOTIFY_ID, notificationCode)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return intent
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun notificationBuilderByO(
        context: Context, title: String?,
        content: String?,
        idIco: Int,
        iconColor: Int,
        AutoCancel: Boolean,
        Ongoing: Boolean,
        applicationContext: Context,
        vibrate: LongArray,
        pendingIntent: PendingIntent?,
        manager: NotificationManager,
        data: NotificationData?,
        priority: Int,
                                      ): Notification.Builder {
        return Notification.Builder(applicationContext, data?.channel?.apiType)
            .setAutoCancel(AutoCancel)
            .setOngoing(Ongoing)
            .setContentTitle(title)
            .setContentText(content)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(idIco)
            .setColor(iconColor) //                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
            .setContentIntent(pendingIntent)
            .setChannelId(data?.channel?.apiType)
            .setTimeoutAfter(data?.ttl ?: app.desty.chat_admin.common.constants.DestyConstants.notificationTTL)
    }

    fun cancelAll() {
        (Utils.getApp()
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            cancelAll()
        }
    }

    fun jumpByChannel(
        channel: ChatNotificationEnum?,
        data: Any?,
        errorInterface: ((channel: ChatNotificationEnum?, data: Any?) -> Unit)?,
                     ) {
        if (data !is String || TextUtils.isEmpty(data)) {
            errorInterface?.invoke(channel, data)
            return
        }
        when (channel ?: errorInterface?.invoke(channel, data)) {
            ChatNotificationEnum.ImMessage -> {
                val pushSession = jsonDecoder.decodeFromString<PushSessionBean>(data)
                app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().routeToHome(
                    HomeRouteBean(
                        routePath = app.desty.chat_admin.common.constants.RouteConstants.Message.sessionDetail,
                        bundle = Bundle().apply {
                            putString("SessionId", pushSession.sessionId)
                        })
                                                                                                    )
            }

            else                           -> errorInterface?.invoke(channel, data)

        }
    }
}