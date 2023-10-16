package app.desty.chat_admin.common.bean

import android.text.InputType
import app.desty.chat_admin.common.enum_bean.DialogInputType
import app.desty.sdk.logcat.Logcat
import com.blankj.utilcode.util.StringUtils

object EditorSetting {

    var multiDisplay: Boolean = false
    var dialogTitle: Int = 0
    var subTitle: Int = 0
    var inputType: Int = 1
    var maxValueList: MutableList<Int> = mutableListOf(100, 100, 100, 100)
    var minValueList: MutableList<Int> = mutableListOf(0, 0, 0, 0)
    var dialogType: DialogInputType? = null

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

    fun clearAll() {
        multiDisplay = false
        dialogTitle = 0
        subTitle = 0
        inputType = 1
        maxValueList = mutableListOf(100, 100, 100, 100)
        minValueList = mutableListOf(0, 0, 0, 0)
        dialogType = null
        Logcat.i("UserConfig:clearAll()")
    }

}