package app.desty.chat_admin.common.utils

import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import app.desty.chat_admin.common.config.AppConfig
import com.blankj.utilcode.util.Utils
import java.util.UUID

object DeviceUtil {// 13 位

    //使用硬件信息+随机UUID生成唯一的deviceId
    //获取Android ID
    fun getDeviceId():String{
            val savedDeviceId = AppConfig.deviceId
            if (!TextUtils.isEmpty(savedDeviceId)) {
                return savedDeviceId
            }

            //获取Android ID
            val android_id = Settings.System.getString(
                Utils.getApp().contentResolver,
                Settings.Secure.ANDROID_ID
                                                      )
            if (!TextUtils.isEmpty(android_id) && android_id != "9774d56d682e549c") {
                AppConfig.deviceId = android_id
                return android_id
            }
            val devIdStr =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.SUPPORTED_ABIS.size % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 // 13 位
            var serial = "serial"
            try {
                serial = Build::class.java.getField("SERIAL")[null].toString()
            } catch (ignore: Exception) {
            }
            val uuid = UUID.randomUUID().toString()

            //使用硬件信息+随机UUID生成唯一的deviceId
            val deviceId = UUID(devIdStr.hashCode().toLong(), serial.hashCode().toLong())
                .toString() + uuid
            AppConfig.deviceId = deviceId
            return deviceId
        }
}