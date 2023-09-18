package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.widget.OnCheckChangeListener;

/**
 * 支持check状态的约束布局，check状态改变时会同步到支持check的子类中
 */
public class CheckConstraintLayout extends ConstraintLayout implements Checkable {
    private              boolean               isCheck           = false;
    private              boolean               touchable         = false;
    private              OnCheckChangeListener onCheckChangeListener;
    private static final int[]                 CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CheckConstraintLayout(Context context) {
        super(context);
    }

    public CheckConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public CheckConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
    }

    /**
     * 获取XML配置
     */
    private void getAttrs(Context context, AttributeSet attributeSet) {
        if (context == null) return;
        if (attributeSet == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CheckConstraintLayout);
        isCheck = typedArray.getBoolean(R.styleable.CheckConstraintLayout_checked, false);
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
        if (isCheck == checked) return;
        for (int i = 0; i < getChildCount(); i++) {
            Object o = getChildAt(i);
            if (o instanceof Checkable) {
                ((Checkable) o).setChecked(checked);
            }
        }
        isCheck = checked;
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onChange();
        }
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void toggle() {
        setChecked(!isCheck);
    }


    public CheckConstraintLayout setOnCheckChangeListener(
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

    public CheckConstraintLayout setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }


    @BindingAdapter("touchable")
    public static void touchable(CheckConstraintLayout view, boolean touchable) {
        view.setTouchable(touchable);
    }

    @BindingAdapter("checked")
    public static void setChecked(CheckConstraintLayout view, boolean check) {
        if (view != null) {
            view.setChecked(check);
        }
    }

    @InverseBindingAdapter(attribute = "checked", event = "checkedAttrChanged")
    public static boolean isChecked(CheckConstraintLayout view) {
        if (view == null) return false;
        return view.isChecked();
    }

    @BindingAdapter(value = "checkedAttrChanged")
    public static void setChangeListener(CheckConstraintLayout view,
                                         InverseBindingListener listener) {
        view.setOnCheckChangeListener(listener::onChange);
    }
}
