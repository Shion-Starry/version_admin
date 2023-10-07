package app.desty.chat_admin.common.widget

import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

open class ChatAdminDialogPopup(context: Context, chatAdminDialog: ChatAdminDialog)
    : CenterPopupView(context), View.OnClickListener {
    private var cancelListener: OnCancelListener? = null
    private var confirmListener: OnConfirmListener? = null
    protected var binding: ViewDataBinding? = null
    private var chatAdminDialog: ChatAdminDialog
    private var httpErrorCode: String? = null
    private var dismissByOption = false

    init {
        bindLayoutId = chatAdminDialog.layoutRes
        this.chatAdminDialog = chatAdminDialog
        addInnerContent()
    }

    override fun getImplLayoutId(): Int =
        if (bindLayoutId != 0) bindLayoutId else R.layout.dialog_admin_yellow

    override fun onCreate() {
        super.onCreate()
        //DataBinding在onCreate方法中调用
        binding = DataBindingUtil.bind(popupImplView)
        if (binding != null) {
            binding!!.setVariable(BR.adminDialog, chatAdminDialog)
            binding!!.setVariable(BR.click, this)
            val tvContent = binding!!.root.findViewById<TextView>(R.id.tv_content)
            if (tvContent != null) {
                tvContent.movementMethod = LinkMovementMethod.getInstance()
            }
            val tvErrorCode = binding!!.root.findViewById<TextView>(R.id.tv_error_code)
            if (tvErrorCode != null) {
                if (!TextUtils.isEmpty(httpErrorCode)) {
                    tvErrorCode.visibility = VISIBLE
                    tvErrorCode.text = StringUtils.format("Error code:[%s]", httpErrorCode)
                } else {
                    tvErrorCode.visibility = GONE
                }
            }
        }
    }

    fun setHttpErrorCode(httpErrorCode: String?): ChatAdminDialogPopup {
        this.httpErrorCode = httpErrorCode
        return this
    }

    fun setListener(
        confirmListener: OnConfirmListener?,
        cancelListener: OnCancelListener?
    ): ChatAdminDialogPopup {
        this.cancelListener = cancelListener
        this.confirmListener = confirmListener
        return this
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tv_cancel) {
            if (cancelListener != null) {
                cancelListener!!.onCancel()
            }
            dismissByOption = true
            dismiss()
        } else if (id == R.id.tv_confirm) {
            if (confirmListener != null) {
                confirmListener!!.onConfirm()
            }
            dismissByOption = true
            if (popupInfo.autoDismiss) {
                dismiss()
            }
        }
    }

    override fun beforeDismiss() {
        super.beforeDismiss()
        if (!dismissByOption && cancelListener != null) {
            cancelListener!!.onCancel()
        }
    }

    override fun getMaxWidth(): Int {
        return ScreenUtils.getAppScreenWidth() - SizeUtils.dp2px(32f)
    }
}