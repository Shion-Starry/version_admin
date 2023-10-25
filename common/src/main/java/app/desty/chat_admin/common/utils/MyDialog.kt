package app.desty.chat_admin.common.utils

import android.content.Context
import android.net.Uri
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import app.desty.chat_admin.common.enum_bean.GlobalDialogEnum
import app.desty.chat_admin.common.widget.ChatAdminDialogPopup
import app.desty.chat_admin.common.widget.GlobalDialog
import app.desty.chat_admin.common.widget.GlobalImageDialog
import app.desty.chat_admin.common.widget.OtpVerifyDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

object MyDialog {

    fun show(
        chatAdminDialog: ChatAdminDialog?,
        onConfirmListener: OnConfirmListener? = null,
        onCancelListener: OnCancelListener? = null
    ): BasePopupView =
        showAdminDialog(null, chatAdminDialog, onConfirmListener, onCancelListener)

    private fun showAdminDialog(
        httpErrorCode: String?,
        chatAdminDialog: ChatAdminDialog?,
        confirmListener: OnConfirmListener?,
        cancelListener: OnCancelListener?
    ): BasePopupView =
        XPopup.Builder(ActivityLifecycleManager.getInstance().topActivity)
            .asCustom(
                ChatAdminDialogPopup(
                    ActivityLifecycleManager.getInstance().topActivity
                ).apply {
                    if (chatAdminDialog != null) {
                        this.chatAdminDialog = chatAdminDialog
                    }
                    this.httpErrorCode = httpErrorCode
                    this.confirmListener = confirmListener
                    this.cancelListener = cancelListener
                }).show()

    fun showOtpDialog(
        confirmListener: ((Boolean) -> Unit)?,
    ): BasePopupView =
        XPopup.Builder(ActivityLifecycleManager.getInstance().topActivity)
            .asCustom(
                OtpVerifyDialog(
                    ActivityLifecycleManager.getInstance().topActivity
                ).apply {
                    this.confirmListener = confirmListener
                }).show()

    fun showDialog(
        dialogEnum: GlobalDialogEnum,
        context:Context? =null,
        config: GlobalDialog.Config? = null,
        block: GlobalDialog.() -> Unit = {}
                  ): BasePopupView? {
        val c = context?: app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().topActivity
        return XPopup.Builder(c)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(
                GlobalDialog(c)
                    .init(dialogEnum, config, block)
                     )
            .show()
    }

    fun showSendImageDialog(
        imageUri: Uri,
        block: GlobalImageDialog.() -> Unit = {}
                           ): BasePopupView? {
        return XPopup.Builder(app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().topActivity)
            .asCustom(
                GlobalImageDialog(app.desty.chat_admin.common.utils.ActivityLifecycleManager.getInstance().topActivity)
                    .init(imageUri, block)
                     )
            .show()
    }
}