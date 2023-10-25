package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.upload.api.UploadApi
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class UploadNewViewModel : BaseVM() {
    val featureTextMap = mapOf<String, MutableLiveData<String>>(
        "channel" to MutableLiveData("android"),
        "latestVersion" to MutableLiveData(""),
        "latestCode" to MutableLiveData(""),
        "compatVersion" to MutableLiveData(""),
        "compatCode" to MutableLiveData(""),
        "url" to MutableLiveData(""),
        "websiteUrl" to MutableLiveData(""),
        "marketUrl" to MutableLiveData(""),
        "content" to MutableLiveData("")
    )
    val canUpload = MediatorLiveData(false)
    var env = MutableLiveData(Environment.Test)
    var ifSuccessful = MutableLiveData(false)

    init {
        for (editorText in featureTextMap.values) {
            canUpload.addSource(editorText) {
                updateUploadEnabled()
            }
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

    fun getSpecified(key: String): MutableLiveData<String> {
        return when (key) {
            "Channel"             -> featureTextMap["channel"] ?: MutableLiveData("")
            "URL"                  -> featureTextMap["url"] ?: MutableLiveData("")
            "Website URL"       -> featureTextMap["websiteUrl"] ?: MutableLiveData("")
            "Market URL"        -> featureTextMap["marketUrl"] ?: MutableLiveData("")
            "Update Content"  -> featureTextMap["content"] ?: MutableLiveData("")
            else                     -> MutableLiveData("")
        }
    }

    fun uploadTestingVer(): suspend CoroutineScope.() -> Unit = {
        val submitResult = Post<Boolean>(UploadApi.saveConfig) {
            json(
                "channel" to (featureTextMap["channel"]?.value ?: ""),
                "latestVersion" to (featureTextMap["latestVersion"]?.value ?: ""),
                "latestCode" to (featureTextMap["latestCode"]?.value?.toLongOrNull() ?: ""),
                "compatibleVersion" to (featureTextMap["compatVersion"]?.value ?: ""),
                "compatibleCode" to (featureTextMap["compatCode"]?.value?.toLongOrNull() ?: ""),
                "url" to (featureTextMap["url"]?.value ?: ""),
                "websiteUrl" to (featureTextMap["websiteUrl"]?.value ?: ""),
                "marketUrl" to (featureTextMap["marketUrl"]?.value ?: ""),
                "content" to (featureTextMap["content"]?.value ?: "")
            )
        }.await()
        if (submitResult) {
            MyToast.showToast("Successfully Submitted")
            ifSuccessful.value = true
        } else {
            MyToast.showToast("Submission Failed :(")
            ifSuccessful.value = false
        }
    }

}