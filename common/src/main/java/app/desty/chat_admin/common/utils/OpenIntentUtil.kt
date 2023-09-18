package app.desty.chat_admin.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import app.desty.chat_admin.sdk_string.R
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils

object OpenIntentUtil {
    /**
     * 打开打电话页面
     * 异常：未安装对应应用时弹出toast
     */
    fun openDial(phoneNumber: String) {
        try {
            PhoneUtils.dial(phoneNumber)
        } catch (e: Exception) {
            //no app found for dial
            app.desty.chat_admin.common.utils.MyToast.showToast(R.string.jump_out_error_base_message, StringUtils.getString(R.string.jump_out_error_dial))
        }
    }

    /**
     * 打开浏览器
     * 异常：未安装对应应用时弹出toast
     */
    fun openWeb(uri: Uri) {
        try {
            Utils.getApp().startActivity(IntentUtils.getBrowserIntent(uri))
        } catch (e: Exception) {
            //no app found for browser
            app.desty.chat_admin.common.utils.MyToast.showToast(R.string.jump_out_error_base_message, StringUtils.getString(R.string.jump_out_error_web))
        }
    }

    /**
     * 打开AppStore
     * 异常：未安装对应应用时弹出toast
     */
    fun gotoAppStore(mActivity: Context, url: String) {
        val appPackage = url.substring(url.lastIndexOf("=") + 1)
        if (url.indexOf("market://") == 0) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                if (intent.resolveActivity(mActivity.packageManager) != null) {
                    Utils.getApp().startActivity(intent)
                } else {
                    // Open Google Play website
                    Utils.getApp().startActivity(
                        IntentUtils.getBrowserIntent(Uri.parse("market://search?q=$appPackage"))
                                                )
                }
            } catch (e: Exception) {
                //no app found for AppStore
                app.desty.chat_admin.common.utils.MyToast.showToast(R.string.jump_out_error_base_message, StringUtils.getString(R.string.jump_out_error_app_store))
            }
        }
    }

}