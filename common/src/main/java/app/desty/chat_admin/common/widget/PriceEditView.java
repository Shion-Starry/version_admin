package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.constants.DestyConstants;
import app.desty.chat_admin.common.utils.MyToast;
import app.desty.chat_admin.common.widget.NoSelectionEditText;
import app.desty.chat_admin.common.constants.DestyConstants;

/**
 * @author xiaoke.lin
 * @date 2022/3/19
 */
public class PriceEditView extends NoSelectionEditText implements TextWatcher {

    private Paint   priceUnitPaint;
    private Paint   priceBgPaint;
    private Paint   viewBgPaint;
    private String  priceUnit              = "Rp";
    private double  maxPrice               = DestyConstants.maxModifierOptionPrice;
    private int     priceUnitTextColor     = ColorUtils.getColor(R.color.grey_400);
    private float   priceUnitTextSize      = ResourceUtils.getDimenByResId(R.dimen.text_sm);
    private int     pricePaddingLeft       = 0;
    private int     priceUnitPaddingLeft   = SizeUtils.dp2px(8);
    private int     priceUnitPaddingRight  = SizeUtils.dp2px(4);
    private int     priceUnitBgColor       = 0;
    private int     priceUnitBgStrokeColor = 0;
    private int     roundRadius            = SizeUtils.dp2px(8);
    private boolean errorValue             = false;


    public PriceEditView(Context context) {
        super(context);
        initPaint();
    }

    public PriceEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public PriceEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        removeTextChangedListener(this);
        addTextChangedListener(this);
        if (priceUnitPaint == null) {
            priceUnitPaint = new Paint();
            priceUnitPaint.setAntiAlias(true);
        }
        priceUnitPaint.setTypeface(getTypeface());
        priceUnitPaint.setColor(priceUnitTextColor);
        priceUnitPaint.setTextSize(priceUnitTextSize);

        if (priceBgPaint == null) {
            priceBgPaint = new Paint();
            priceBgPaint.setAntiAlias(true);
        }
        priceBgPaint.setColor(priceUnitBgColor);

        if (viewBgPaint == null) {
            viewBgPaint = new Paint();
            viewBgPaint.setAntiAlias(true);
        }

        float width = priceUnitPaint.measureText(priceUnit);
        int paddingEnd = getPaddingEnd();
        setPadding((int) (priceUnitPaddingLeft + priceUnitPaddingRight + width + SizeUtils.dp2px(2) + pricePaddingLeft), 0, paddingEnd, 0);
        setSingleLine();
        setKeyListener(DigitsKeyListener.getInstance("1234567890.,"));
    }

    @BindingAdapter(value = {"priceUnit", "priceUnitTextColor", "priceUnitTextSize",
                             "priceUnitPaddingLeft", "priceUnitPaddingRight", "pricePaddingLeft",
                             "priceUnitBgColor", "priceUnitBgStrokeColor",
                             "roundRadius", "maxPrice", "errorValue"}, requireAll = false)
    public static void setByBinding(PriceEditView view, String priceUnit, int priceTextColor,
                                    float priceTextSize,
                                    int priceUnitPaddingLeft, int priceUnitPaddingRight,
                                    int pricePaddingLeft,
                                    int priceUnitBgColor, int priceUnitBgStrokeColor,
                                    int roundRadius,
                                    double maxPrice, boolean errorValue) {
        if (view == null) return;
        if (priceTextColor != 0) view.priceUnitTextColor = priceTextColor;
        if (priceTextSize != 0) view.priceUnitTextSize = priceTextSize;
        if (!TextUtils.isEmpty(priceUnit)) view.priceUnit = priceUnit;
        if (maxPrice > 0) view.maxPrice = maxPrice;
        view.pricePaddingLeft       = SizeUtils.dp2px(pricePaddingLeft);
        view.priceUnitPaddingLeft   = SizeUtils.dp2px(priceUnitPaddingLeft);
        view.priceUnitPaddingRight  = SizeUtils.dp2px(priceUnitPaddingRight);
        view.priceUnitBgColor       = priceUnitBgColor;
        view.priceUnitBgStrokeColor = priceUnitBgStrokeColor;
        view.roundRadius            = SizeUtils.dp2px(roundRadius);
        view.errorValue             = errorValue;
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
        float unitWidth = priceUnitPaint.measureText(priceUnit);
        if (priceUnitBgColor != 0) {
            priceBgPaint.setColor(priceUnitBgColor);
            float[] r = new float[]{roundRadius, roundRadius, 0, 0, 0, 0, roundRadius, roundRadius};
            Path path = new Path();
            path.addRoundRect(scrollX + dp2, dp2, scrollX + priceUnitPaddingRight + priceUnitPaddingLeft + unitWidth + dp2, getHeight() - dp2, r, Path.Direction.CW);
            canvas.drawPath(path, priceBgPaint);
        }
        if (priceUnitBgStrokeColor != 0) {
            priceBgPaint.setColor(priceUnitBgStrokeColor);
            canvas.drawRect(scrollX + priceUnitPaddingRight + priceUnitPaddingLeft + unitWidth + dp1, dp2,
                            scrollX + priceUnitPaddingRight + priceUnitPaddingLeft + unitWidth + dp2, getHeight() - dp2,
                            priceBgPaint);
        }
        viewBgPaint.setStyle(Paint.Style.STROKE);
        if (focused) {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.saphire_500));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(2));
        } else {
            viewBgPaint.setColor(ColorUtils.getColor(errorValue
                                                     ? R.color.red_500
                                                     : R.color.grey_300));
            viewBgPaint.setStrokeWidth(SizeUtils.dp2px(1));
        }
        canvas.drawRoundRect(scrollX + dp2, dp2, scrollX + getWidth() - dp2, getHeight() - dp2, roundRadius, roundRadius, viewBgPaint);

        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = priceUnitPaint.getFontMetrics();
        float textHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;
        canvas.drawText(priceUnit, priceUnitPaddingLeft + scrollX + dp2, getHeight() / 2 + textHeight, priceUnitPaint);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    boolean isEditing = false;

    @Override
    public void afterTextChanged(Editable s) {
        if (isEditing) return;
        isEditing = true;

        String str = s.toString().replaceAll("[^\\d]", "");
        double s1 = 0;
        if (!TextUtils.isEmpty(str)) {
            double tmp = Double.parseDouble(str);
            if(tmp>maxPrice){

                MyToast.showToast(app.desty.chat_admin.sdk_string.R.string.over_max_limit);
            }
            s1 = Math.min(maxPrice, tmp);

            NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
            ((DecimalFormat) nf2).applyPattern("###,###");
            s.replace(0, s.length(), nf2.format(s1).replace(',', '.'));
        }else {
            if(s.length()>0){
                s.clear();
            }
        }

        isEditing = false;
    }
}
