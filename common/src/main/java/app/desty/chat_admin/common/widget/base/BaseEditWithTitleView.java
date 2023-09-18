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
public class BaseEditWithTitleView extends NoSelectionEditText {

    private Paint titlePaint;
    private Paint titleBgPaint;
    float[] titleBgRadii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
    Path    titleBgPath  = new Path();
    private Paint   viewBgPaint;
    private String  titleStr           = "\uD83C\uDDF2\uD83C\uDDE8+62";
    private int     titleTextColor     = ColorUtils.getColor(R.color.grey_400);
    private float   titleTextSize      = ResourceUtils.getDimenByResId(R.dimen.text_sm);
    private int     contentPaddingLeft = 0;
    private int     titlePaddingLeft   = SizeUtils.dp2px(8);
    private int     titlePaddingRight  = SizeUtils.dp2px(4);
    private int     titleBgColor       = 0;
    private int     titleBgStrokeColor = 0;
    private int     roundRadius        = SizeUtils.dp2px(8);
    private boolean errorValue         = false;


    public BaseEditWithTitleView(Context context) {
        super(context);
        initPaint();
    }

    public BaseEditWithTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public BaseEditWithTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        if (titlePaint == null) {
            titlePaint = new Paint();
            titlePaint.setAntiAlias(true);
        }
        titlePaint.setTypeface(getTypeface());
        titlePaint.setColor(titleTextColor);
        titlePaint.setTextSize(titleTextSize);

        if (titleBgPaint == null) {
            titleBgPaint = new Paint();
            titleBgPaint.setAntiAlias(true);
        }
        titleBgPaint.setColor(titleBgColor);

        if (viewBgPaint == null) {
            viewBgPaint = new Paint();
            viewBgPaint.setAntiAlias(true);
        }

        float width = titlePaint.measureText(titleStr);
        int paddingEnd = getPaddingEnd();
        setPadding((int) (titlePaddingLeft + titlePaddingRight + width + SizeUtils.dp2px(2) + contentPaddingLeft), 0, paddingEnd, 0);
        setSingleLine();
//        setKeyListener(DigitsKeyListener.getInstance("1234567890.,"));
    }

    @BindingAdapter(value = {"title", "titleTextColor", "titleTextSize",
                             "titlePaddingLeft", "titlePaddingRight", "contentPaddingLeft",
                             "titleBgColor", "titleBgStrokeColor",
                             "roundRadius", "errorValue"}, requireAll = false)
    public static void setByBinding(BaseEditWithTitleView view, String title,
                                    int priceTextColor,
                                    float priceTextSize,
                                    int titlePaddingLeft, int titlePaddingRight,
                                    int contentPaddingLeft,
                                    int titleBgColor, int titleBgStrokeColor,
                                    int roundRadius, boolean errorValue) {
        if (view == null) return;
        if (priceTextColor != 0) view.titleTextColor = priceTextColor;
        if (priceTextSize != 0) view.titleTextSize = priceTextSize;
        if (!TextUtils.isEmpty(title)) view.titleStr = title;
        view.contentPaddingLeft = SizeUtils.dp2px(contentPaddingLeft);
        view.titlePaddingLeft   = SizeUtils.dp2px(titlePaddingLeft);
        view.titlePaddingRight  = SizeUtils.dp2px(titlePaddingRight);
        view.titleBgColor       = titleBgColor;
        view.titleBgStrokeColor = titleBgStrokeColor;
        view.roundRadius        = SizeUtils.dp2px(roundRadius);
        view.errorValue         = errorValue;
        view.initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int scrollX = getScrollX();
        boolean focused = isFocused();
        int dp2 = SizeUtils.dp2px(1) / (focused
                                        ? 1
                                        : 2);
        int dp1 = dp2 / 2 / (focused
                             ? 1
                             : 2);
        viewBgPaint.setStyle(Paint.Style.FILL);
        viewBgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(scrollX + dp2, dp2, scrollX + getWidth() - dp2, getHeight() - dp2, roundRadius, roundRadius, viewBgPaint);

        float unitWidth = titlePaint.measureText(titleStr);
        if (titleBgColor != 0) {
            titleBgPaint.setColor(titleBgColor);
            titleBgRadii[0] = roundRadius;
            titleBgRadii[1] = roundRadius;
            titleBgRadii[6] = roundRadius;
            titleBgRadii[7] = roundRadius;
            titleBgPath.addRoundRect(scrollX + dp2, dp2, scrollX + titlePaddingRight + titlePaddingLeft + unitWidth + dp2, getHeight() - dp2, titleBgRadii, Path.Direction.CW);
            canvas.drawPath(titleBgPath, titleBgPaint);
        }
        if (titleBgStrokeColor != 0) {
            titleBgPaint.setColor(titleBgStrokeColor);
            canvas.drawRect(scrollX + titlePaddingRight + titlePaddingLeft + unitWidth + dp1, dp2,
                            scrollX + titlePaddingRight + titlePaddingLeft + unitWidth + dp2, getHeight() - dp2,
                            titleBgPaint);
        }
        viewBgPaint.setStyle(Paint.Style.STROKE);
        if (focused) {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.chat_500));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(1.5f));
        } else {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.grey_300));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(1));
        }

        canvas.drawRoundRect(scrollX + dp2, dp2, scrollX + getWidth() - dp2, getHeight() - dp2, roundRadius, roundRadius, viewBgPaint);

        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float textHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;
        canvas.drawText(titleStr, titlePaddingLeft + scrollX + dp2, (getHeight() >> 1) + textHeight, titlePaint);

    }
}
