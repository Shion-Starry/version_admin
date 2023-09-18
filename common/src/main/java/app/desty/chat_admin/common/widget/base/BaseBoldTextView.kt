package app.desty.chat_admin.common.widget.base

import android.content.Context
import android.util.AttributeSet

open class BaseBoldTextView : BaseTextView {

    override var isBold = true

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
                                                                )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
               ) : super(context!!, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //默认使用加粗字体进行测量宽度
        setTextBold(true)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setTextBold(this.isBold)
    }
}