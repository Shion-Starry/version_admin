package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.KeyboardUtils;

import app.desty.chat_admin.common.widget.base.BaseEditText;
import app.desty.chat_admin.common.widget.base.BaseEditText;

/**
 * @author xiaoke.lin
 * @date 2022/3/17
 */
public class NoSelectionEditText extends BaseEditText {
    private boolean selectable = false;

    public NoSelectionEditText(Context context) {
        super(context);
    }

    public NoSelectionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSelectionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onSelectionChanged(int start, int end) {
        if (!selectable) {
            CharSequence text = getText();
            if (text != null) {
                if (start != text.length() || end != text.length()) {
                    setSelection(text.length(), text.length());
                    return;
                }
            }
        }

        super.onSelectionChanged(start, end);
    }


    @BindingAdapter(value = {"selectable"})
    public static void setSelectionAble(NoSelectionEditText noSelectionEditText,
                                        boolean selectable) {
        noSelectionEditText.selectable = selectable;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) {
            KeyboardUtils.hideSoftInput(this);
        } else {
            KeyboardUtils.showSoftInput(this);
        }
    }
}
