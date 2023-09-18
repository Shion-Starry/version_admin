package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import app.desty.chat_admin.common.widget.base.BaseTextView;
import app.desty.chat_admin.common.widget.base.BaseTextView;

public class AutoSizeTextView extends BaseTextView {
    private int autoSmallLength = 10;
    private int smallTextSize   = 14;
    private int largeTextSize   = 24;

    public AutoSizeTextView(@NonNull Context context) {
        super(context);
    }

    public AutoSizeTextView(@NonNull Context context,
                            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null && text.length() >= autoSmallLength) {
            setTextSize(smallTextSize);
        } else {
            setTextSize(largeTextSize);
        }
        super.setText(text, type);
    }

    @BindingAdapter(value = {"autoSmallLength", "smallTextSize",
                             "largeTextSize", "text"}, requireAll = false)
    public static void setConfig(AutoSizeTextView textView, int autoSmallLength, int smallTextSize,
                                 int largeTextSize, CharSequence text) {
        if (textView == null) return;
        textView.autoSmallLength = autoSmallLength;
        textView.smallTextSize   = smallTextSize;
        textView.largeTextSize   = largeTextSize;
        textView.setText(text);
    }
}
