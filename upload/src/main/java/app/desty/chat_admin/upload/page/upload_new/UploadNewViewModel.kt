package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.VersionInfo

class UploadNewViewModel : BaseVM() {
    val verInfo = MutableLiveData<VersionInfo>()
    val canUpload = MutableLiveData(false)
    val showInfo = MutableLiveData(true)
}