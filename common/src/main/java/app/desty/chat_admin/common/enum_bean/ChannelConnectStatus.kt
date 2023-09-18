package app.desty.chat_admin.common.enum_bean

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils

enum class ChannelConnectStatus(
    @ColorRes val shopNameColorRes: Int = 0,
    @ColorRes val bgColorRes: Int = 0,
    @ColorRes val textColorRes: Int = 0,
    @StringRes val textRes: Int = 0,
                               ) {
    All,
    Connected(
        shopNameColorRes = R.color.grey_900,
        bgColorRes = R.color.chat_100,
        textColorRes = R.color.chat_600,
        textRes = R.string.channel_connect_status_connected
             ),
    Expired(
        shopNameColorRes = R.color.grey_300,
        bgColorRes = R.color.red_100,
        textColorRes = R.color.red_600,
        textRes = R.string.channel_connect_status_expired
           ),
    Waiting(
        shopNameColorRes = R.color.grey_900,
        bgColorRes = R.color.sunflower_50,
        textColorRes = R.color.sunflower_500,
        textRes = R.string.channel_connect_status_waiting
           ),
    ;

    fun getShopNameColor() = ColorUtils.getColor(shopNameColorRes)
    fun getText(): String = StringUtils.getString(textRes) ?: ""
    fun getBgColor(): Int = ColorUtils.getColor(bgColorRes)
    fun getTextColor(): Int = ColorUtils.getColor(textColorRes)
}