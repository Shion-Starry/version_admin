package app.desty.chat_admin.common.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import app.desty.chat_admin.common.R

/**
 * iOS风格的加载对话框
 * @param title 加载对话框的标题
 */
class LoadingDialog @JvmOverloads constructor(
    context: Context,
    var title: String = "",
    @StyleRes themeResId: Int = R.style.LoadingDialog,
                                             ) : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}