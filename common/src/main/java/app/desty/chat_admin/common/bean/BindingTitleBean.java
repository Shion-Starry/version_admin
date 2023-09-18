package app.desty.chat_admin.common.bean;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;

import app.desty.chat_admin.common.R;

/**
 * @author xiaoke.lin
 * @date 2022/3/1
 */
public class BindingTitleBean {

    private int statusBarHeight = 0;

    // 背景
    @ColorInt
    private int background = ColorUtils.getColor(R.color.saphire_700);

    // 标题
    private String title;
    @ColorInt
    private int titleTextColor = ColorUtils.getColor(R.color.color_white);
    private boolean titleTextBold = false;

    // 返回键
    private boolean backShow = false;
    @ColorInt
    private int backTintColor = ColorUtils.getColor(R.color.color_white);
    @DrawableRes
    private int backIconResId = 0;
    private View.OnClickListener backClick;

    // 菜单键
    private boolean menuShow = false;
    @ColorInt
    private int menuTintColor = ColorUtils.getColor(R.color.color_white);
    @DrawableRes
    private int menuIconResId = 0;

    @DrawableRes
    private int rightOperationIconResId = 0;

    @DrawableRes
    private int rightOperationIconResId2 = 0;

    private View.OnClickListener menuClick;

    private boolean showRightImage = false;
    private boolean showRightImage2 = false;
    // 右侧文本
    private boolean showRightText = false;
    private boolean rightButtonEnable = true;
    private int rightButtonTextRes = R.string.save;
    @ColorInt
    private int rightButtonTextColor = ColorUtils.getColor(R.color.saphire_700);
    @ColorInt
    private int rightButtonDisableTextColor = ColorUtils.getColor(R.color.grey_300);
    private View.OnClickListener rightTextClick;
    private View.OnClickListener rightImageClick;
    private View.OnClickListener rightImageClick2;

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public BindingTitleBean setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
        return this;
    }

    public int getBackground() {
        return background;
    }

    public BindingTitleBean setBackground(int background) {
        this.background = background;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BindingTitleBean setTitleRes(int titleRes) {
        this.title = StringUtils.getString(titleRes);
        return this;
    }

    public BindingTitleBean setTitleResAdd(int titleRes, String text) {
        this.title = StringUtils.getString(titleRes) + " " + text;
        return this;
    }

    public BindingTitleBean setTitleRes(String title) {
        this.title = title;
        return this;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public BindingTitleBean setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public boolean isShowRightImage() {
        return showRightImage;
    }

    public BindingTitleBean setShowRightImage(boolean showRightImage) {
        this.showRightImage = showRightImage;
        return this;
    }

    public boolean isShowRightImage2() {
        return showRightImage2;
    }

    public BindingTitleBean setShowRightImage2(boolean showRightImage2) {
        this.showRightImage2 = showRightImage2;
        return this;
    }

    public boolean isTitleTextBold() {
        return titleTextBold;
    }

    public BindingTitleBean setTitleTextBold(boolean titleTextBold) {
        this.titleTextBold = titleTextBold;
        return this;
    }

    public boolean isBackShow() {
        return backShow;
    }

    public BindingTitleBean setBackShow(boolean backShow) {
        this.backShow = backShow;
        return this;
    }

    public int getBackTintColor() {
        return backTintColor;
    }

    public BindingTitleBean setBackTintColor(int backTintColor) {
        this.backTintColor = backTintColor;
        return this;
    }

    public int getBackIconResId() {
        return backIconResId;
    }

    public BindingTitleBean setBackIconResId(int backIconResId) {
        this.backIconResId = backIconResId;
        return this;
    }

    public View.OnClickListener getBackClick() {
        return backClick;
    }

    public BindingTitleBean setBackClick(View.OnClickListener backClick) {
        this.backClick = backClick;
        return this;
    }

    public boolean isMenuShow() {
        return menuShow;
    }

    public BindingTitleBean setMenuShow(boolean menuShow) {
        this.menuShow = menuShow;
        return this;
    }

    public int getMenuTintColor() {
        return menuTintColor;
    }

    public BindingTitleBean setMenuTintColor(int menuTintColor) {
        this.menuTintColor = menuTintColor;
        return this;
    }

    public int getMenuIconResId() {
        return menuIconResId;
    }

    public BindingTitleBean setMenuIconResId(int menuIconResId) {
        this.menuIconResId = menuIconResId;
        return this;
    }


    public int getRightOperationIconResId() {
        return rightOperationIconResId;
    }

    public BindingTitleBean setRightOperationIconResId(int rightOperationIconResId) {
        this.rightOperationIconResId = rightOperationIconResId;
        return this;
    }

    public int getRightOperationIconResId2() {
        return rightOperationIconResId2;
    }

    public BindingTitleBean setRightOperationIconResId2(int rightOperationIconResId2) {
        this.rightOperationIconResId2 = rightOperationIconResId2;
        return this;
    }

    public View.OnClickListener getMenuClick() {
        return menuClick;
    }

    public BindingTitleBean setMenuClick(View.OnClickListener menuClick) {
        this.menuClick = menuClick;
        return this;
    }


    public boolean isShowRightText() {
        return showRightText;
    }

    public BindingTitleBean setShowRightText(boolean showRightText) {
        this.showRightText = showRightText;
        return this;
    }

    public boolean isRightButtonEnable() {
        return rightButtonEnable;
    }

    public BindingTitleBean setRightButtonEnable(boolean rightButtonEnable) {
        this.rightButtonEnable = rightButtonEnable;
        return this;
    }

    public int getRightButtonTextRes() {
        return rightButtonTextRes;
    }

    public BindingTitleBean setRightButtonTextRes(int rightButtonTextRes) {
        this.rightButtonTextRes = rightButtonTextRes;
        return this;
    }

    public int getRightButtonTextColor() {
        return rightButtonTextColor;
    }

    public BindingTitleBean setRightButtonTextColor(int rightButtonTextColor) {
        this.rightButtonTextColor = rightButtonTextColor;
        return this;
    }

    public int getRightButtonDisableTextColor() {
        return rightButtonDisableTextColor;
    }

    public BindingTitleBean setRightButtonDisableTextColor(int rightButtonDisableTextColor) {
        this.rightButtonDisableTextColor = rightButtonDisableTextColor;
        return this;
    }

    public View.OnClickListener getRightTextClick() {
        return rightTextClick;
    }

    public BindingTitleBean setRightTextClick(View.OnClickListener rightTextClick) {
        this.rightTextClick = rightTextClick;
        return this;
    }


    public View.OnClickListener getRightImageClick() {
        return rightImageClick;
    }

    public BindingTitleBean setRightImageClick(View.OnClickListener rightImageClick) {
        this.rightImageClick = rightImageClick;
        return this;
    }

    public View.OnClickListener getRightImageClick2() {
        return rightImageClick2;
    }

    public BindingTitleBean setRightImageClick2(View.OnClickListener rightImageClick2) {
        this.rightImageClick2 = rightImageClick2;
        return this;
    }
}
