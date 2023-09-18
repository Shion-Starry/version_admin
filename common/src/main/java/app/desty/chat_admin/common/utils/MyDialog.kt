package app.desty.chat_admin.common.utils

import android.content.Context
import android.net.Uri
import app.desty.chat_admin.common.enum_bean.GlobalDialogEnum
import app.desty.chat_admin.common.widget.GlobalDialog
import app.desty.chat_admin.common.widget.GlobalImageDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

object MyDialog {

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