package app.desty.chat_admin.common.bean

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.databinding.BaseObservable
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.ColorUtils

data class ToolbarConfig(
    var statusBarHeight: Int = 0,

    // 背景
    @ColorInt
    var background: Int = ColorUtils.getColor(R.color.chat_100),
    // 标题
    var title: String = "",
    @ColorInt
    var titleTextColor: Int = ColorUtils.getColor(R.color.grey_900),
    var titleTextBold: Boolean = false,

    // 返回按钮
    var showBack: Boolean = false,
    @ColorInt
    var backTintColor: Int = ColorUtils.getColor(R.color.grey_900),
    @DrawableRes
    var backIconResId: Int = R.drawable.ic_back_tint,
    var backClick: View.OnClickListener? = null,

    // 菜单按钮
    var showMenu: Boolean = false,
    @ColorInt
    var menuTintColor: Int = ColorUtils.getColor(R.color.grey_900),
    @DrawableRes
    var menuIconResId: Int = R.drawable.ic_menu_tint,
    var menuClick: View.OnClickListener? = null,

    // 右侧icon按钮
    var showRightOperateIcon: Boolean = false,
    @DrawableRes
    var rightOperateIconResId: Int = 0,
    @ColorInt
    var rightOperateIconTint: Int = ColorUtils.getColor(R.color.grey_900),
    var rightOperateClick: View.OnClickListener? = null,

    // 右侧文字按钮
    var showRightTextOperate: Boolean = false,
    var rightTextStr: String = "",
    var rightTextClick: View.OnClickListener? = null,

    ) : BaseObservable()
