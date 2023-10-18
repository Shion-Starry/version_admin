package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.config.Environment

class UploadNewViewModel : BaseVM() {
    val featureTextMap = mapOf<String, MutableLiveData<String>>(
        "channel" to MutableLiveData("android"),
        "latestVersion" to MutableLiveData("1"),
        "latestCode" to MutableLiveData("2"),
        "compatVersion" to MutableLiveData("3"),
        "compatCode" to MutableLiveData("4"),
        "url" to MutableLiveData("5"),
        "websiteUrl" to MutableLiveData("6"),
        "marketUrl" to MutableLiveData("7"),
        "content" to MutableLiveData("8")
    )
    val canUpload = MediatorLiveData(false)
    var env = MutableLiveData(Environment.Test)

    init {

        for (editorText in featureTextMap.values) {
            canUpload.addSource(editorText) { updateUploadEnabled() }
        }

    }

    private fun updateUploadEnabled() {
        for (text in featureTextMap.values) {
            if (text.value.isNullOrBlank()) {
                canUpload.value = false
                return
            }
        }
        canUpload.value = true
    }

}