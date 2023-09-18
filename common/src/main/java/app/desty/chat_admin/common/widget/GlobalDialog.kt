package app.desty.chat_admin.common.widget

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.config.UserConfig
import app.desty.chat_admin.common.databinding.DialogGlobalBinding
import app.desty.chat_admin.common.enum_bean.GlobalDialogEnum
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.CenterPopupView

class GlobalDialog(context: Context) : CenterPopupView(context) {

    private var binding: DialogGlobalBinding? = null
    private var dialogEnum: GlobalDialogEnum? = null
    private var onClickPositive: OnClickListener? = null
    private var onClickNegative: OnClickListener? = null

    private var config: Config? = null

    fun init(dialogEnum: GlobalDialogEnum, config: Config?, block: GlobalDialog.() -> Unit = {}): GlobalDialog {
        this.dialogEnum = dialogEnum
        this.config = config
        this.block()
        return this
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_global

    override fun onCreate() {
        super.onCreate()

        binding = DataBindingUtil.bind(popupImplView)
        binding?.apply {
            data = Config(
                config?.title ?: dialogEnum?.getTitleStr() ?: "",
                config?.content ?: dialogEnum?.getContentStr() ?: "",
                config?.positive ?: dialogEnum?.getPositiveStr() ?: "",
                config?.negative ?: dialogEnum?.getNegativeStr() ?: ""
                         )
            click = ClickProxy()
        }
    }

    fun clickPositive(onClickListener: OnClickListener) {
        this.onClickPositive = onClickListener
    }

    fun clickNegative(onClickListener: OnClickListener) {
        this.onClickNegative = onClickListener
    }

    inner class ClickProxy {
        fun clickNegative(view: View) {
            onClickNegative?.onClick(view)
            dismiss()
        }

        fun clickPositive(view: View) {
            onClickPositive?.onClick(view)
            dismiss()
        }
    }

    data class Config(
        val title: String? = null,
        val content: String? = null,
        val positive: String? = null,
        val negative: String? = null,
                     )
}