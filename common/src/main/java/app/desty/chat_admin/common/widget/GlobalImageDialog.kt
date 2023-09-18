package app.desty.chat_admin.common.widget

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.databinding.DataBindingUtil
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.databinding.DialogGlobalImageBinding
import app.desty.chat_admin.common.enum_bean.GlobalDialogEnum
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.CenterPopupView

class GlobalImageDialog(context: Context) : CenterPopupView(context) {

    private var binding: DialogGlobalImageBinding? = null
    private var onClickPositive: OnClickListener? = null
    private var onClickNegative: OnClickListener? = null
    private var imageUri:Uri? = null

    fun init(imageUri: Uri, block: GlobalImageDialog.() -> Unit = {}): GlobalImageDialog {
        this.imageUri = imageUri
        this.block()
        return this
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_global_image

    override fun onCreate() {
        super.onCreate()

        binding = DataBindingUtil.bind(popupImplView)
        binding?.apply {
            title = "Send photo"
            imageUri = this@GlobalImageDialog.imageUri
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


}