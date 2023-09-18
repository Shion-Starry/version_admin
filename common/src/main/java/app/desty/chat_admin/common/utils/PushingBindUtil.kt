package app.desty.chat_admin.common.utils

import android.text.TextUtils
import app.desty.chat_admin.common.api.Api
import app.desty.chat_admin.common.config.UserConfig
import com.blankj.utilcode.util.AppUtils
import com.drake.net.Net
import com.drake.net.scope.AndroidScope
import com.drake.net.utils.scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

object PushingBindUtil {

    private const val TYPE_BIND = 1
    private const val TYPE_UNBIND = 2
    private const val NET_GROUP_ID = "PushingBind"
    private var currentPushId: String? = null//当前推送ID
    private var bindScope: AndroidScope? = null
    private var unbindScope: AndroidScope? = null


    fun autoBindOrUnbind(pushId: String?) {
        //重置网络请求的生命周期
        bindScope?.cancel()
        bindScope = null
        unbindScope?.cancel()
        unbindScope = null
        Net.cancelGroup(NET_GROUP_ID)

        //发起新的请求
        this.currentPushId = pushId
        original(calculateBindType(pushId))
    }

    private fun original(type: Int) {
        when (type) {
            TYPE_BIND   -> bind(currentPushId)
            TYPE_UNBIND -> unbind()
        }
    }

    private fun calculateBindType(pushId: String?): Int {
        if (TextUtils.isEmpty(UserConfig.token)) {
            return TYPE_UNBIND
        } else {
            if (pushId == null) return 0
            if (TextUtils.isEmpty(pushId)) return 0
            return TYPE_BIND
        }
    }


    //请求绑定接口
    private fun bind(pushId: String?) {
        bindScope = scope(dispatcher = Dispatchers.IO) {
            var isSuccess = false
            var sleepTime = 1000L
            while (!isSuccess) {
                try {
                    Net.post(app.desty.chat_admin.common.api.Api.bindPush) {
                        setGroup(NET_GROUP_ID)
                        json(
                            "bizType" to 2,
                            "appId" to AppUtils.getAppPackageName(),
                            "deviceId" to DeviceUtil.getDeviceId(),
                            "userId" to UserConfig.imAccount,
                            "token" to pushId
                            )
                    }.execute<String>()
                    //没报错就是成功
                    isSuccess = true
                } catch (ignore: Exception) {
                    delay(sleepTime)
                    sleepTime *= 2//指数退避
                }
            }
        }
    }

    //请求解绑接口
    private fun unbind() {
        unbindScope = scope(dispatcher = Dispatchers.IO) {
            var isSuccess = false
            var sleepTime = 1000L
            while (!isSuccess) {
                try {
                    Net.post(app.desty.chat_admin.common.api.Api.unbindPush) {
                        setGroup(NET_GROUP_ID)
                        json(
                            "bizType" to 2,
                            "appId" to AppUtils.getAppPackageName(),
                            "deviceId" to DeviceUtil.getDeviceId(),
                            )
                    }.execute<String>()
                    //没报错就是成功
                    isSuccess = true
                } catch (ignore: Exception) {
                    delay(sleepTime)
                    sleepTime *= 2//指数退避
                }
            }
        }
    }

}