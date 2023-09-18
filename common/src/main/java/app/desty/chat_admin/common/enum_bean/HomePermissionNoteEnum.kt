package app.desty.chat_admin.common.enum_bean

import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.StringUtils

enum class HomePermissionNoteEnum(
    private val titleRes: Int,
    private val contentRes: Int,
    private val operateTextRes: Int,

    ) {
    Notification(
        titleRes = R.string.permission_notification_title,
        contentRes = R.string.permission_notification_content,
        operateTextRes = R.string.permission_notification_operate
                ),  //通知栏权限
    BatteryOptimize(
        titleRes = R.string.permission_battery_title,
        contentRes = R.string.permission_battery_content,
        operateTextRes = R.string.permission_notification_operate,
                   ),  //电池优化权限
//    FloatingWindow(
//        R.string.allow_show_floating_window,
//        R.string.allow_show_floating_window_setting
//                  ), //悬浮窗
    ;

    fun getTitleStr(): String = StringUtils.getString(titleRes)
    fun getContentStr(): String = StringUtils.getString(contentRes)
    fun getOperateTextStr(): String = StringUtils.getString(operateTextRes)
}