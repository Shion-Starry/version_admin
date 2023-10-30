package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.upload.api.UploadApi
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class UploadNewViewModel : BaseVM() {
    val featureTextMap = mapOf<String, MutableLiveData<String>>(
        "channel" to MutableLiveData(""),
        "latestVersion" to MutableLiveData(""),
        "latestCode" to MutableLiveData(""),
        "compatVersion" to MutableLiveData(""),
        "compatCode" to MutableLiveData(""),
        "url" to MutableLiveData(""),
        "websiteUrl" to MutableLiveData(""),
        "marketUrl" to MutableLiveData(""),
        "content" to MutableLiveData("")
    )
//    val realTimeInfo = MutableLiveData(VersionInfo())
    val canUpload = MediatorLiveData(false)
    val env = MutableLiveData(Environment.Test)
    val ifSuccessful = MutableLiveData(false)

    init {
//        canUpload.addSource(realTimeInfo) { updateUploadEnabled() }

        for (editorText in featureTextMap.values) {
            canUpload.addSource(editorText) {
                updateUploadEnabled()
            }
        }
    }

    private fun updateUploadEnabled() {
//        canUpload.value = !(realTimeInfo.value?.channel.isNullOrBlank()
//                || realTimeInfo.value?.compatCode == 0
//                || realTimeInfo.value?.compatVersion.isNullOrBlank()
//                || realTimeInfo.value?.content.isNullOrBlank()
//                || realTimeInfo.value?.latestCode == 0
//                || realTimeInfo.value?.latestVersion.isNullOrBlank()
//                || realTimeInfo.value?.marketUrl.isNullOrBlank()
//                || realTimeInfo.value?.url.isNullOrBlank()
//                || realTimeInfo.value?.websiteUrl.isNullOrBlank())
        for (text in featureTextMap.values) {
            if (text.value.isNullOrBlank()) {
                canUpload.value = false
                return
            }
        }
        canUpload.value = true
    }

    fun getVersionInfo(versionCode: Long): String {
        val versionGroup = VersionGroup(versionCode)
        return versionGroup.getVersionStr()
    }

    fun getVersionCode(versionCode: Long): String {
        val versionGroup = VersionGroup(versionCode)
        return versionGroup.getVersionCodeStr()
    }

    fun getSpecified(key: String): MutableLiveData<String> {
        return when (key) {
            "Channel"             -> featureTextMap["channel"] ?: MutableLiveData("")
            "URL"                  -> featureTextMap["url"] ?: MutableLiveData("")
            "Website URL"       -> featureTextMap["websiteUrl"] ?: MutableLiveData("")
            "Market URL"        -> featureTextMap["marketUrl"] ?: MutableLiveData("")
            "Update Content"  -> featureTextMap["content"] ?: MutableLiveData("")
            else                     -> MutableLiveData("")
//            "Channel"             -> featureTextMap["channel"] ?: MutableLiveData("")
//            "URL"                  -> featureTextMap["url"] ?: MutableLiveData("")
//            "Website URL"       -> featureTextMap["websiteUrl"] ?: MutableLiveData("")
//            "Market URL"        -> featureTextMap["marketUrl"] ?: MutableLiveData("")
//            "Update Content"  -> featureTextMap["content"] ?: MutableLiveData("")
//            else                     -> MutableLiveData("")
        }
    }

    fun uploadNewVer(): suspend CoroutineScope.() -> Unit = {
        val submitResult = Post<Boolean>("${EnvConfig.getBaseUrl(env.value)}${UploadApi.saveVersion}") {
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