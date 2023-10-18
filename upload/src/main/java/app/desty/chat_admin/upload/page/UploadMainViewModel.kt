package app.desty.chat_admin.upload.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.VersionInfo
import app.desty.chat_admin.upload.api.UploadApi
import com.drake.net.Get

class UploadMainViewModel : BaseVM() {
    val verInfo = MutableLiveData<VersionInfo>()
    val showInfo = MutableLiveData(true)

    fun getVersionInfo() {
        scopeNetLife {
            val versionInfo = Get<VersionInfo>(UploadApi.getConfig) {
                param("channel", "android")
            }.await()
            verInfo.value = versionInfo
        }
    }

}