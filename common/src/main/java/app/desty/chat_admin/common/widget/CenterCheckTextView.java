package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.widget.OnCheckChangeListener;
import app.desty.chat_admin.common.widget.base.BaseTextView;
import app.desty.chat_admin.common.widget.base.BaseTextView;

public class CenterCheckTextView extends BaseTextView implements Checkable {
    private              boolean               isCheck           = false;
    private              boolean               touchable         = false;
    private              OnCheckChangeListener onCheckChangeListener;
    private static final int[]                 CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CenterCheckTextView(@NonNull Context context) {
        super(context);
    }

    public CenterCheckTextView(@NonNull Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterCheckTextView(@NonNull Context context, @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckTextView);
        isCheck = typedArray.getBoolean(R.styleable.CheckImageView_checked, false);
        typedArray.recycle();
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

    @Override
    protected void onDraw(Canvas canvas) {
        int gravity = getGravity();
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            if (drawableLeft != null) {
                if ((gravity | Gravity.CENTER_HORIZONTAL) > 0) {
                    setGravity(gravity & ~Gravity.CENTER_HORIZONTAL);
                }
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            } else if (drawableRight != null) {
                if ((gravity | Gravity.CENTER_HORIZONTAL) > 0) {
                    setGravity((gravity & ~Gravity.CENTER_HORIZONTAL) | Gravity.END);
                }
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate(-(getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }


    public CenterCheckTextView setOnCheckChangeListener(
            OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchable && event.getAction() == MotionEvent.ACTION_DOWN) {
            setChecked(!isCheck);
        }
        return super.onTouchEvent(event);
    }

    public CenterCheckTextView setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }


    @BindingAdapter("touchable")
    public static void touchable(CenterCheckTextView view, boolean touchable) {
        view.setTouchable(touchable);
    }

    @BindingAdapter("checked")
    public static void setChecked(CenterCheckTextView view, boolean check) {
        if (view != null) {
            view.setChecked(check);
        }
    }

    @InverseBindingAdapter(attribute = "checked", event = "checkedAttrChanged")
    public static boolean isChecked(CenterCheckTextView view) {
        if (view == null) return false;
        return view.isChecked();
    }

    @BindingAdapter(value = "checkedAttrChanged")
    public static void setChangeListener(CenterCheckTextView view,
                                         InverseBindingListener listener) {
        view.setOnCheckChangeListener(listener::onChange);
    }
}
