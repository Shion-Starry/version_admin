package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;

import app.desty.chat_admin.common.R;


/**
 * @author linxk
 * @date 2020/11/7
 * <p>
 * 背景带阴影的自定义控件
 */
public class ShadowConstraintLayout extends ConstraintLayout {

    private static final int DEFAULT_CORNER_RADIUS = 8;
    private static final int DEFAULT_SHADOW_RADIUS = 8;
    private static final String DEFAULT_SHADOW_COLOR = "#3D000000";

    /**
     * 背景色
     */
    private int mBgColor;
    /**
     * 阴影颜色
     */
    private int mShadowColor;
    /**
     * 阴影圆角半径
     */
    private float mShadowRadius;
    /**
     * 背景圆角半径
     */
    private float mCornerRadius;
    private float mCornerRadiusLT;
    private float mCornerRadiusRT;
    private float mCornerRadiusLB;
    private float mCornerRadiusRB;
    /**
     * 背景圆角矩阵
     */
    float[] radii ={0,0,0,0,0,0,0,0};
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

    private boolean mDrawLeft = true;
    private boolean mDrawRight = true;
    private boolean mDrawTop = true;
    private boolean mDrawBottom = true;


    public ShadowConstraintLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setPadding(mDrawLeft ? leftPadding : 0, mDrawTop ? topPadding : 0, mDrawRight ? rightPadding : 0, mDrawBottom ? bottomPadding : 0);
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
        TypedArray attr = getTypedArray(context, attrs, R.styleable.ShadowConstraintLayout);
        if (attr == null) {
            return;
        }

        try {
            mCornerRadius = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_cornerRadius,0);
            mCornerRadiusLT = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_cornerRadiusLT, 0);
            mCornerRadiusRT = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_cornerRadiusRT, 0);
            mCornerRadiusLB = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_cornerRadiusLB, 0);
            mCornerRadiusRB = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_cornerRadiusRB, 0);
            if(mCornerRadius>0){
                radii=new float[]{mCornerRadius,mCornerRadius,mCornerRadius,mCornerRadius,mCornerRadius,mCornerRadius,mCornerRadius,mCornerRadius};
            }else {
                radii=new float[]{mCornerRadiusLT,mCornerRadiusLT,mCornerRadiusRT,mCornerRadiusRT,mCornerRadiusRB,mCornerRadiusRB,mCornerRadiusLB,mCornerRadiusLB};
            }
            mShadowRadius = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_shadowRadius, SizeUtils.dp2px(DEFAULT_SHADOW_RADIUS));
            mDLeft = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_dLeft, 0);
            mDRight = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_dRight, 0);
            mDTop = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_dTop, 0);
            mDBottom = attr.getDimension(R.styleable.ShadowConstraintLayout_sl_dBottom, 0);
            mShadowColor = attr.getColor(R.styleable.ShadowConstraintLayout_sl_shadowColor, Color.parseColor(DEFAULT_SHADOW_COLOR));
            mDrawLeft = attr.getBoolean(R.styleable.ShadowConstraintLayout_sl_drawLeft, true);
            mDrawRight = attr.getBoolean(R.styleable.ShadowConstraintLayout_sl_drawRight, true);
            mDrawTop = attr.getBoolean(R.styleable.ShadowConstraintLayout_sl_drawTop, true);
            mDrawBottom = attr.getBoolean(R.styleable.ShadowConstraintLayout_sl_drawBottom, true);
            mIsCircle = attr.getBoolean(R.styleable.ShadowConstraintLayout_sl_isCircle, true);
            mBgColor = attr.getColor(R.styleable.ShadowConstraintLayout_sl_bgColor, Color.WHITE);

        } finally {
            attr.recycle();
        }
    }

    //设置背景色
    public void setBgColor(@ColorInt int bgColor){
        mBgColor = bgColor;
        if (getWidth() == 0 && getHeight() == 0) return;
        setBackgroundCompat(getWidth(),getHeight());
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius, float dLeft, float dTop,
                                      float dRight, float dBottom, int shadowColor, int fillColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                mDrawLeft ? shadowRadius : 0,
                mDrawTop ? shadowRadius : 0,
                shadowWidth - (mDrawRight ? shadowRadius : 0),
                shadowHeight - (mDrawBottom ? shadowRadius : 0));

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

        Paint clearPaint = new Paint();
        clearPaint.setColor(fillColor);

        if (mIsCircle) {
            Path path = new Path();
            path.addRoundRect(shadowRect,radii, Path.Direction.CW);
            canvas.drawPath(path,shadowPaint);
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

}
