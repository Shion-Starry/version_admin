package app.desty.chat_admin.common.widget.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.BindingAdapter
import app.desty.chat_admin.common.utils.FontUtil

open class BaseEditText : AppCompatEditText {

    private var isBold = false

    constructor(context: Context?) : super(context!!) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
                                                                ) {
        init(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
               ) : super(context!!, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context?) {
        setTextBold(this.isBold)
    }

    fun setTextBold(isBold: Boolean?) {
        this.isBold = isBold ?: false
        app.desty.chat_admin.common.utils.FontUtil.setFount(this, this.isBold)
    }

    
    companion object {
        @BindingAdapter(value = ["isBold"])
        @JvmStatic
        fun setTextBold(view: BaseEditText, isBold: Boolean?) {
            view.setTextBold(isBold)
        }
    }

}