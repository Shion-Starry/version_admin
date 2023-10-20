package app.desty.chat_admin.upload.page

import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.VersionInfo
import app.desty.chat_admin.upload.api.UploadApi
import com.blankj.utilcode.util.TimeUtils
import com.drake.net.Get
import kotlinx.coroutines.CoroutineScope

class UploadMainViewModel : BaseVM() {
    val verInfo = MutableLiveData<VersionInfo>()
    val showInfo = MutableLiveData(true)
    val updatedTime =MutableLiveData("")

    fun getVersionInfo(): suspend CoroutineScope.() -> Unit = {
        val versionInfo = Get<VersionInfo>(UploadApi.getConfig) {
            param("channel", "android")
        }.await()
        verInfo.value = versionInfo
        resetUpdatedTime()
    }

    private fun resetUpdatedTime() {
        val currentTime = System.currentTimeMillis()
        updatedTime.value = TimeUtils.millis2String(currentTime,"yyyy-MM-dd HH:mm:ss")
    }

}