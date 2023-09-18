package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.SizeUtils;

import app.desty.chat_admin.common.R;


/**
 * @author linxk
 * @date 2020/11/7
 * <p>
 * 背景带阴影的自定义控件
 */
public class ShadowLayout extends LinearLayout {

    private static final int    DEFAULT_CORNER_RADIUS = 8;
    private static final int    DEFAULT_SHADOW_RADIUS = 8;
    private static final String DEFAULT_SHADOW_COLOR  = "#3D000000";

    /**
     * 背景色
     */
    private int   mBgColor;
    /**
     * 阴影颜色
     */
    private int   mShadowColor;
    /**
     * 阴影圆角半径
     */
    private float mShadowRadius;
    /**
     * 背景圆角半径
     */
    private float mCornerRadius;
    /**
     * leftpadding
     */
    private float mDLeft;
    /**
     * rightpadding
     */
    private float mDRight;
    /**
     * toppadding
     */
    private float mDTop;
    /**
     * bottompadding
     */
    private float mDBottom;

    /**
     * 是否在尺寸变化时重绘阴影
     */
    private boolean mInvalidateShadowOnSizeChanged = true;

    /**
     * 是否为圆角矩形
     */
    private boolean mIsCircle = true;

    private boolean mDrawLeft   = true;
    private boolean mDrawRight  = true;
    private boolean mDrawTop    = true;
    private boolean mDrawBottom = true;


    public ShadowLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (getBackground() == null || mInvalidateShadowOnSizeChanged)) {
            setBackgroundCompat(w, h);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);

        int leftPadding = (int) (mShadowRadius + Math.abs(mDLeft));
        int bottomPadding = (int) (mShadowRadius + Math.abs(mDBottom));
        int topPadding = (int) (mShadowRadius + Math.abs(mDTop));
        int rightPadding = (int) (mShadowRadius + Math.abs(mDRight));
        setPadding(mDrawLeft
                   ? leftPadding
                   : 0, mDrawTop
                        ? topPadding
                        : 0, mDrawRight
                             ? rightPadding
                             : 0, mDrawBottom
                                  ? bottomPadding
                                  : 0);
    }


    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDLeft, mDTop, mDRight, mDBottom, mShadowColor, mBgColor);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }


    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, SizeUtils.dp2px(DEFAULT_CORNER_RADIUS));
            mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, SizeUtils.dp2px(DEFAULT_SHADOW_RADIUS));
            mDLeft        = attr.getDimension(R.styleable.ShadowLayout_sl_dLeft, 0);
            mDRight       = attr.getDimension(R.styleable.ShadowLayout_sl_dRight, 0);
            mDTop         = attr.getDimension(R.styleable.ShadowLayout_sl_dTop, 0);
            mDBottom      = attr.getDimension(R.styleable.ShadowLayout_sl_dBottom, 0);
            mShadowColor  = attr.getColor(R.styleable.ShadowLayout_sl_shadowColor, Color.parseColor(DEFAULT_SHADOW_COLOR));
            mDrawLeft     = attr.getBoolean(R.styleable.ShadowLayout_sl_drawLeft, true);
            mDrawRight    = attr.getBoolean(R.styleable.ShadowLayout_sl_drawRight, true);
            mDrawTop      = attr.getBoolean(R.styleable.ShadowLayout_sl_drawTop, true);
            mDrawBottom   = attr.getBoolean(R.styleable.ShadowLayout_sl_drawBottom, true);
            mIsCircle     = attr.getBoolean(R.styleable.ShadowLayout_sl_isCircle, true);
            mBgColor      = attr.getColor(R.styleable.ShadowLayout_sl_bgColor, Color.WHITE);

        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius,
                                      float shadowRadius, float dLeft, float dTop,
                                      float dRight, float dBottom, int shadowColor, int fillColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                mDrawLeft
                ? shadowRadius
                : 0,
                mDrawTop
                ? shadowRadius
                : 0,
                shadowWidth - (mDrawRight
                               ? shadowRadius
                               : 0),
                shadowHeight - (mDrawBottom
                                ? shadowRadius
                                : 0));

//        shadowRect.top -= (dTop > 0 ? dTop : Math.abs(dTop));
//        shadowRect.bottom -= (dBottom > 0 ? dBottom : Math.abs(dBottom));
//        shadowRect.left -= (dLeft > 0 ? dLeft : Math.abs(dLeft));
//        shadowRect.right -= (dRight > 0 ? dRight : Math.abs(dRight));

        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, 0, SizeUtils.dp2px(2), shadowColor);
        }

        if (mIsCircle) {
            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        } else {
            canvas.drawRect(shadowRect, shadowPaint);
        }
        return output;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
    }


    public void setDrawLeft(boolean mDrawLeft) {
        this.mDrawLeft = mDrawLeft;
    }

    public void setDrawRight(boolean mDrawRight) {
        this.mDrawRight = mDrawRight;
    }


    public void setDrawTop(boolean mDrawTop) {
        this.mDrawTop = mDrawTop;
    }


    public void setDrawBottom(boolean mDrawBottom) {
        this.mDrawBottom = mDrawBottom;
    }

    @BindingAdapter(value = {"sl_bgColor"}, requireAll = false)
    public static void setParams(ShadowLayout shadowLayout, int slBgColor) {
        if (shadowLayout == null) return;
        shadowLayout.mBgColor = slBgColor;
        shadowLayout.invalidate();
    }
}
