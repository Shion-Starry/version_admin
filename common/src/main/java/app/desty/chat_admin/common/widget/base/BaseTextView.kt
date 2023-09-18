package app.desty.chat_admin.common.widget.base

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.utils.FontUtil
import app.desty.sdk.logcat.Logcat

open class BaseTextView : AppCompatTextView {

    protected open var isBold = false
    protected val TEXT_STYLE_NORMAL = 5
    protected val TEXT_STYLE_BOLD = 10


    constructor(context: Context?) : super(context!!) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
                                                                ) {
        init(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
               ) : super(context!!, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        context?.obtainStyledAttributes(attrs, R.styleable.BaseTextView, 0, 0)?.apply {
            when (getInt(R.styleable.BaseTextView_desty_text_style, TEXT_STYLE_NORMAL)) {
                TEXT_STYLE_NORMAL -> setTextBold(false)
                TEXT_STYLE_BOLD   -> setTextBold(true)
            }

            recycle()
        } ?: {
            setTextBold(this.isBold)
        }
    }

    fun setTextBold(isBold: Boolean?) {
        this.isBold = isBold ?: false
        app.desty.chat_admin.common.utils.FontUtil.setFount(this, this.isBold)
    }


    companion object {

        @BindingAdapter(value = ["isBold"])
        @JvmStatic
        fun setTextBold(view: BaseTextView, isBold: Boolean?) {
            view.setTextBold(isBold)
        }

        @BindingAdapter(value = ["movementMethod"])
        @JvmStatic
        fun setMovement(view:BaseTextView,movementMethod: MovementMethod){
            view.movementMethod = movementMethod
        }
    }

}