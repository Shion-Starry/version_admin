package app.desty.chat_admin.common.enum_bean

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils

enum class GptStatus(
    val apiType:Int = 0,
    @ColorRes val bgColorRes: Int = 0,
    @StringRes val textRes: Int = 0,
    @ColorRes val textColorRes: Int = 0,
                    ) {
    Default(bgColorRes = R.color.chat_200, textRes = R.string.message_operation_gpt_reply, textColorRes = R.color.grey_900),

    Generating(apiType = 1,bgColorRes = R.color.grey_200, textRes = R.string.message_operation_gpt_generating, textColorRes = R.color.grey_500)

    ;

    fun getBgColor() = ColorUtils.getColor(bgColorRes)
    fun getShowText() = StringUtils.getString(textRes)?:""
    fun getTextColor() = ColorUtils.getColor(textColorRes)
}