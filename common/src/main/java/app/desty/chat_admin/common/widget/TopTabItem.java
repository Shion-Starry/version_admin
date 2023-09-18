package app.desty.chat_admin.common.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;

import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

import app.desty.chat_admin.common.widget.base.BaseTextView;
import app.desty.chat_admin.common.R;
import app.desty.chat_admin.common.widget.base.BaseTextView;

/**
 * @author xiaoke.lin
 * @date 2022/3/4
 */
public class TopTabItem extends FrameLayout implements IMeasurablePagerTitleView {

    protected int[] mTextSelectedColor;
    protected int[] mTextNormalColor;
    protected Integer[] mCountTextSelectedColor;
    protected int[] mCountTextNormalColor;
    protected int titleTextRes;////文字资源
    protected String titleTextStr;////文字字符串
    protected int count;//数量
    protected int minWidth = -1;//最小宽度


    public TopTabItem(@NonNull Context context, int minWidth) {
        super(context);
        this.minWidth = minWidth;
    }

    /**
     * 外部直接将布局设置进来
     *
     * @param contentView
     */
    public void setContentView(View contentView) {
        setContentView(contentView, null);
    }

    public void setContentView(View contentView, LayoutParams lp) {
        removeAllViews();
        if (contentView != null) {
            if (lp == null) {
                lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;
                lp.leftMargin = SizeUtils.dp2px(16);
                lp.rightMargin = SizeUtils.dp2px(16);
            }

            addView(contentView, lp);
            if (minWidth > 0) {
                setMinimumWidth(SizeUtils.dp2px(minWidth));
            }
        }
    }

    public void setContentView(int layoutId) {
        View child = LayoutInflater.from(getContext()).inflate(layoutId, null);
        setContentView(child, null);
    }


    @Override
    public int getContentLeft() {
        return getLeft() + getMeasuredWidth() / 2 - getChildAt(0).getMeasuredWidth() / 2;
    }

    @Override
    public int getContentTop() {
        return getHeight() / 2 - getMeasuredHeight() / 2;
    }

    @Override
    public int getContentRight() {
        return getRight() - getMeasuredWidth() / 2 + getChildAt(0).getMeasuredWidth() / 2;
    }

    @Override
    public int getContentBottom() {
        return getHeight() / 2 + getMeasuredHeight() / 2;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        ((BaseTextView) findViewById(R.id.tab_item_title)).setTextBold(true);
        ((BaseTextView) findViewById(R.id.tab_item_count)).setTextBold(true);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        ((BaseTextView) findViewById(R.id.tab_item_title)).setTextBold(false);
        ((BaseTextView) findViewById(R.id.tab_item_count)).setTextBold(false);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        int textColor = 0;
        if (mTextSelectedColor != null && mTextSelectedColor.length > 0 && mTextNormalColor != null && mTextNormalColor.length > 0) {
            int tmpTextSelectedColor = mTextSelectedColor[Math.min(index, mTextSelectedColor.length - 1)];
            int tmpTextNormalColor = mTextNormalColor[Math.min(index, mTextNormalColor.length - 1)];
            textColor = ArgbEvaluatorHolder.eval(leavePercent, tmpTextSelectedColor, tmpTextNormalColor);
        }
        int countColor = 0;
        if (mCountTextSelectedColor != null && mCountTextSelectedColor.length > 0 && mCountTextNormalColor != null && mCountTextNormalColor.length > 0) {
            int tmpTextSelectedColor = mCountTextSelectedColor[Math.min(index, mCountTextSelectedColor.length - 1)];
            int tmpTextNormalColor = mCountTextNormalColor[Math.min(index, mCountTextNormalColor.length - 1)];
            countColor = ArgbEvaluatorHolder.eval(leavePercent, tmpTextSelectedColor, tmpTextNormalColor);
        }
        ((TextView) findViewById(R.id.tab_item_title)).setTextColor(textColor);
        ((TextView) findViewById(R.id.tab_item_count)).setTextColor(countColor);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

        int textColor = 0;
        if (mTextSelectedColor != null && mTextSelectedColor.length > 0 && mTextNormalColor != null && mTextNormalColor.length > 0) {
            int tmpTextSelectedColor = mTextSelectedColor[Math.min(index, mTextSelectedColor.length - 1)];
            int tmpTextNormalColor = mTextNormalColor[Math.min(index, mTextNormalColor.length - 1)];
            textColor = ArgbEvaluatorHolder.eval(enterPercent, tmpTextNormalColor, tmpTextSelectedColor);
        }
        int countColor = 0;
        if (mCountTextSelectedColor != null && mCountTextSelectedColor.length > 0 && mCountTextNormalColor != null && mCountTextNormalColor.length > 0) {
            int tmpTextSelectedColor = mCountTextSelectedColor[Math.min(index, mCountTextSelectedColor.length - 1)];
            int tmpTextNormalColor = mCountTextNormalColor[Math.min(index, mCountTextNormalColor.length - 1)];
            countColor = ArgbEvaluatorHolder.eval(enterPercent, tmpTextNormalColor, tmpTextSelectedColor);
        }
        ((TextView) findViewById(R.id.tab_item_title)).setTextColor(textColor);
        ((TextView) findViewById(R.id.tab_item_count)).setTextColor(countColor);
    }

    public int[] getTextSelectedColor() {
        return mTextSelectedColor;
    }

    public TopTabItem setTextSelectedColor(int... textSelectedColor) {
        this.mTextSelectedColor = textSelectedColor;
        return this;
    }

    public int[] getTextNormalColor() {
        return mTextNormalColor;
    }

    public TopTabItem setTextNormalColor(int... textNormalColor) {
        this.mTextNormalColor = textNormalColor;
        return this;
    }

    public Integer[] getCountBackColor() {
        return mCountTextSelectedColor;
    }

    public TopTabItem setCountBackSelectedColor(Integer... countBackSelectedColor) {
        this.mCountTextSelectedColor = countBackSelectedColor;
        return this;
    }

    public int[] getCountBackNormalColor() {
        return mCountTextNormalColor;
    }

    public TopTabItem setCountBackNormalColor(int... countNormalColor) {
        this.mCountTextNormalColor = countNormalColor;
        return this;
    }

    public int getTitleTextRes() {
        return titleTextRes;
    }

    public TopTabItem setTitleTextRes(int titleTextRes) {
        ((TextView) findViewById(R.id.tab_item_title)).setText(titleTextRes);
        this.titleTextRes = titleTextRes;
        return this;
    }

    public TopTabItem setTitleTextStr(String titleTextStr) {
        ((TextView) findViewById(R.id.tab_item_title)).setText(titleTextStr);
        this.titleTextStr = titleTextStr;
        return this;
    }

    public int getCount() {
        return count;
    }

    public TopTabItem setCount(int count) {
        TextView viewById = findViewById(R.id.tab_item_count);
        viewById.setVisibility(count > 0
                ? VISIBLE
                : GONE);
        viewById.setText(String.valueOf(count));
        this.count = count;
        return this;
    }

}
