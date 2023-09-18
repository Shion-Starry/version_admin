package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;

import java.util.ArrayList;

public class SessionDetailLayout extends LinearLayout {


    public SessionDetailLayout(@NonNull Context context) {
        super(context);
    }

    public SessionDetailLayout(@NonNull Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SessionDetailLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            isShouldHideInput(v, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    // 获取当前焦点控件
    public View getCurrentFocus() {
        ArrayList<View> focusables = getFocusables(View.FOCUS_FORWARD);
        for (View focusable : focusables) {
            if (focusable.hasFocus()) return focusable;
        }
        return null;
    }

    // 判断是否点击在外部
    public void isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            ViewGroup parent = (ViewGroup)v.getParent();
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            parent.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + parent.getHeight();
            int right = left + parent.getWidth();
            boolean clickOut =
                    !(event.getX() > left && event.getX() < right
                            && event.getY() > top && event.getY() < bottom);
            if (clickOut) {
                KeyboardUtils.hideSoftInput(v);
                v.clearFocus();
            }
        }
    }
}
