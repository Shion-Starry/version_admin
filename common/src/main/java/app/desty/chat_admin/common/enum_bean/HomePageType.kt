package app.desty.chat_admin.common.enum_bean

import android.os.Parcelable
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.ColorUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HomePageType(
    val id: Int,
    val iconRes: Int,
    val titleStringRes: Int,
    val fragmentPath: String,
    val navBarColorRes: Int,
    val rightIconRes: Int = 0,
    val rightIconRes2: Int = 0
) : Parcelable {
    Logout(
        8000,
        R.drawable.ic_navi_logout,
        R.string.navigation_logout,
        "",
        0);

    @IgnoredOnParcel
    val navBarColor: Int =
        if (navBarColorRes != 0) {
            ColorUtils.getColor(navBarColorRes)
        } else {
            0
        }
}