package app.desty.chat_admin.common.enum_bean

import android.os.Parcelable
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.constants.RouteConstants
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
    val rightIconRes: Int = 0
) : Parcelable {
    Home(
        1000,
        R.drawable.ic_navi_home,
        R.string.navigation_home,
        RouteConstants.Home.homeState,
        0
    ),
    Upload(
        2000,
        R.drawable.ic_navi_upload,
        R.string.latest_release_info_title,
        RouteConstants.Upload.main,
        0,
        R.drawable.ic_refresh_tint
    ),
    Cloud(
        3000,
        R.drawable.ic_navi_cloud,
        R.string.navigation_cloud,
        RouteConstants.Cloud.main,
        0,
        R.drawable.ic_arrow_up_tint
    ),
    Hidden(
        4000,
        R.drawable.ic_eye,
        R.string.navigation_hidden,
        RouteConstants.Hidden.main,
        0
    ),
    Logout(
        8000,
        R.drawable.ic_navi_logout,
        R.string.navigation_logout,
        "",
        0
    );

    @IgnoredOnParcel
    val navBarColor: Int =
        if (navBarColorRes != 0) {
            ColorUtils.getColor(navBarColorRes)
        } else {
            0
        }
}