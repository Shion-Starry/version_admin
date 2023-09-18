package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.widget.OnCheckChangeListener;


public class CheckImageView extends androidx.appcompat.widget.AppCompatImageView implements Checkable {
    private              boolean               isCheck           = false;
    private              boolean               touchable         = false;
    private              OnCheckChangeListener onCheckChangeListener;
    private static final int[]                 CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CheckImageView(Context context) {
        super(context);
    }

    public CheckImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckImageView);
        isCheck = typedArray.getBoolean(R.styleable.CheckImageView_checked, false);
        typedArray.recycle();
    }

    public CheckImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckImageView);
        isCheck = typedArray.getBoolean(R.styleable.CheckImageView_checked, false);
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchable && event.getAction() == MotionEvent.ACTION_DOWN) {
            setChecked(!isCheck);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isCheck != checked) {
            isCheck = checked;
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onChange();
            }
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void toggle() {
        setChecked(!isCheck);
    }

    public CheckImageView setOnCheckChangeListener(
            OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
        return this;
    }

    public CheckImageView setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }

    @BindingAdapter("touchable")
    public static void touchable(CheckImageView view, boolean touchable) {
        view.setTouchable(touchable);
    }

    @BindingAdapter("checked")
    public static void setChecked(CheckImageView view, boolean check) {
        if (view != null) {
            view.setChecked(check);
        }
    }

    @InverseBindingAdapter(attribute = "checked", event = "checkedAttrChanged")
    public static boolean isChecked(CheckImageView view) {
        if (view == null) return false;
        return view.isChecked();
    }

    @BindingAdapter(value = "checkedAttrChanged")
    public static void setChangeListener(CheckImageView view, InverseBindingListener listener) {
        view.setOnCheckChangeListener(listener::onChange);
    }
}
