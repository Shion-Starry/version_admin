package app.desty.chat_admin.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.SwitchCompat

class CustomSwitch : SwitchCompat {

    private var action: (() -> Boolean)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var canClick: Boolean = false

    private var lastClickTime: Long = 0


    fun setClickAction(clickAction: () -> Boolean) {
        action = clickAction
    }

    override fun toggle() {
        //两次点击间隔超过300ms才给执行performClick时间
        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - lastClickTime) > 300) {
            lastClickTime = currentTime
            if (action == null) {
                super.toggle()
            } else {
                action?.invoke()?.apply {
                    if (this && !canClick) {
                        canClick = true
                        super.toggle()
                    }
                }
            }
        } else {
            lastClickTime = currentTime
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        action = null
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        //过滤move事件
        if (ev?.action == MotionEvent.ACTION_MOVE) return false
        return super.onTouchEvent(ev)
    }


}