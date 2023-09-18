package app.desty.chat_admin.common.enum_bean

import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.StringUtils

enum class GlobalDialogEnum(
    val titleRes: Int = 0,
    val contentRes: Int = 0,
    val negativeRes: Int = R.string.cancel,
    val positiveRes: Int = R.string.yes,
                           ) {
    NoInternet(
        titleRes = R.string.splash_no_internet_title,
        contentRes = R.string.splash_no_internet_content,
        positiveRes = R.string.splash_no_internet_positive,
        negativeRes = R.string.splash_no_internet_negative,
              ),

    ResendMessage(
        titleRes = R.string.message_resend_title,
        positiveRes = R.string.message_resend_positive
                 ),
    NoSessionPermission(
        titleRes = R.string.message_no_permission_title,
        positiveRes = R.string.message_no_permission_positive,
        negativeRes = 0
                       ),
    Bind3rdPartSuccess(
        titleRes = R.string.bind_3rd_part_success_title,
        contentRes = R.string.bind_3rd_part_success_content,
        positiveRes = R.string.bind_3rd_part_success_positive,
        negativeRes = 0,
                      ),
    Bind3rdPartFailure(
        titleRes = R.string.bind_3rd_part_failure_title,
        positiveRes = R.string.bind_3rd_part_failure_positive,
        negativeRes = R.string.bind_3rd_part_failure_negative,
                      ),
    DeleteChannelConfirm(
        titleRes = R.string.channel_operation_delete_confirm_title,
        positiveRes = R.string.yes,
        negativeRes = R.string.cancel
                        ),

    LoginOut(
        titleRes = R.string.login_out_dialog_title,
        contentRes = R.string.login_out_dialog_content,
            ),

    ;

    fun getTitleStr(): String = if (titleRes != 0) StringUtils.getString(titleRes) else ""

    fun getContentStr(): String = if (contentRes != 0) StringUtils.getString(contentRes) else ""

    fun getNegativeStr(): String = if (negativeRes != 0) StringUtils.getString(negativeRes) else ""

    fun getPositiveStr(): String = if (positiveRes != 0) StringUtils.getString(positiveRes) else ""
}