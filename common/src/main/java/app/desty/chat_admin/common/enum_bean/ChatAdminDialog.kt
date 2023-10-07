package app.desty.chat_admin.common.enum_bean

import android.text.Html
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.StringUtils

enum class ChatAdminDialog(
    val layoutRes: Int = R.layout.dialog_admin_yellow,
    val titleRes: Int = 0,
    var titleFormat: Array<Any> = emptyArray(),
    val subTitleRes: Int = 0,
    var subTitleFormat: Array<Any> = emptyArray(),
    val contentRes: Int = 0,
    var contentFormat: Array<Any> = emptyArray(),
    val cancelRes: Int = R.string.cancel,
    val okRes: Int = R.string.ok
) {
    Logout(
        layoutRes = R.layout.dialog_admin_red,
        titleRes = R.string.logout_dialog_title,
        subTitleRes = R.string.logout_dialog_subtitle
    );

    fun getFinalTitle(): String {
        if (titleRes !=0) {
            return StringUtils.getString(titleRes, *titleFormat)
        }
        return ""
    }

    fun getFinalSubTitle(): String {
        if (subTitleRes != 0) {
            return StringUtils.getString(subTitleRes, *subTitleFormat)
        }
        return ""
    }

    fun getFinalContent(): CharSequence {
        if (contentRes != 0) {
            return Html.fromHtml(StringUtils.getString(contentRes, *contentFormat))
        }
        return ""
    }

}