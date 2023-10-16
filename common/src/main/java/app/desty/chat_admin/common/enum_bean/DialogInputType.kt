package app.desty.chat_admin.common.enum_bean

import android.os.Parcelable
import android.text.InputType
import app.desty.chat_admin.common.R
import com.blankj.utilcode.util.StringUtils
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DialogInputType(
    val multiDisplay: Boolean = false,
    val dialogTitle: Int = 0,
    val subTitle: Int = 0,
    val inputType: Int = 1
) : Parcelable {
    VersionId(
        false,
        R.string.edit_title_version_id,
        R.string.edit_subtitle_version_id,
        1
    ),
    VersionName(
        false,
        R.string.edit_title_version_name,
        R.string.edit_subtitle_version_name,
        1
    ),
    ConfigValue(
        false,
        R.string.edit_title_config_value,
        R.string.edit_subtitle_config_value,
        1
    ),
    LatestVersion(
        true,
        R.string.edit_title_latest_version,
        R.string.edit_subtitle_version_setting,
        2
    ),
    CompatVersion(
        true,
        R.string.edit_title_compat_version,
        R.string.edit_subtitle_version_setting,
        2
    ),
    Channel(
        false,
        R.string.edit_title_channel,
        R.string.edit_subtitle_channel,
        1
    );

    fun getFinalTitle(): String {
        return if (dialogTitle != 0) {
            StringUtils.getString(dialogTitle)
        } else {
            ""
        }
    }

    fun getFinalSubTitle(): String {
        return if (subTitle != 0) {
            StringUtils.getString(subTitle)
        } else {
            ""
        }
    }

    fun getFinalInputType(): Int {
        return when (inputType) {
            1 -> InputType.TYPE_CLASS_TEXT
            2 -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_CLASS_TEXT
        }
    }

}