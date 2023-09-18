package app.desty.chat_admin.common.widget.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.widget.NoSelectionEditText;
import app.desty.sdk.logcat.Logcat;

/**
 * @author xiaoke.lin
 * @date 2022/3/19
 */
public class BaseEditWithoutTitleView extends NoSelectionEditText {

    private Paint   viewBgPaint;
    private int     roundRadius = SizeUtils.dp2px(8);
    private boolean errorValue  = false;


    public BaseEditWithoutTitleView(Context context) {
        super(context);
        initPaint();
    }

    public BaseEditWithoutTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public BaseEditWithoutTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {

        if (viewBgPaint == null) {
            viewBgPaint = new Paint();
            viewBgPaint.setAntiAlias(true);
        }
    }

    @BindingAdapter(value = {"roundRadius", "errorValue"}, requireAll = false)
    public static void setByBinding(BaseEditWithoutTitleView view,
                                    int roundRadius, boolean errorValue) {
        if (view == null) return;
        view.roundRadius = SizeUtils.dp2px(roundRadius);
        view.errorValue  = errorValue;
        view.initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int scrollX = getScrollX();
        boolean focused = isFocused();
        int dp2 = SizeUtils.dp2px(1) / (focused
                                        ? 1
                                        : 2);
        viewBgPaint.setStyle(Paint.Style.FILL);
        viewBgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(scrollX + dp2, dp2, scrollX + getWidth() - dp2, getHeight() - dp2, roundRadius, roundRadius, viewBgPaint);

        viewBgPaint.setStyle(Paint.Style.STROKE);
        if (focused) {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.chat_500));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(2f));
        } else {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.grey_300));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(1));
        }
        canvas.drawRoundRect(scrollX + dp2, dp2, scrollX + getWidth() - dp2, getHeight() - dp2, roundRadius, roundRadius, viewBgPaint);

        super.onDraw(canvas);
    }
}
