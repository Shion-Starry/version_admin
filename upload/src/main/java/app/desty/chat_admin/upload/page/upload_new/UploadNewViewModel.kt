package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.upload.api.UploadApi
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class UploadNewViewModel : BaseVM() {
    val channel = MutableLiveData("")
    val latestVersion = MutableLiveData("")
    val latestCode = MutableLiveData("")
    val compatVersion = MutableLiveData("")
    val compatCode = MutableLiveData("")
    val url = MutableLiveData("")
    val websiteUrl = MutableLiveData("")
    val marketUrl = MutableLiveData("")
    private val content = MutableLiveData("")
    private val infoList = listOf(
        channel,
        latestVersion,
        latestCode,
        compatVersion,
        compatCode,
        url,
        websiteUrl,
        marketUrl,
        content)
    val canUpload = MediatorLiveData(false)
    val env = MutableLiveData(Environment.Test)
    val ifSuccessful = MutableLiveData(false)

    init {
        for (info in infoList) {
            canUpload.addSource(info) {
                updateUploadEnabled()
            }
        }
    }

    private fun updateUploadEnabled() {
        for (text in infoList) {
            if (text.value.isNullOrBlank()) {
                canUpload.value = false
                return
            }
        }
        canUpload.value = true
    }

    fun getSpecified(key: String): MutableLiveData<String> {
        return when (key) {
            "Channel"             -> channel
            "URL"                  -> url
            "Website URL"       -> websiteUrl
            "Market URL"        -> marketUrl
            "Update Content"  -> content
            else                     -> MutableLiveData("")
        }
    }

    fun uploadNewVer(): suspend CoroutineScope.() -> Unit = {
        val submitResult = Post<Boolean>("${EnvConfig.getBaseUrl(env.value)}${UploadApi.saveVersion}") {
            json(
                "channel" to (channel.value),
                "latestVersion" to (latestVersion.value),
                "latestCode" to (latestCode.value?.toInt() ?: 0),
                "compatibleVersion" to (compatVersion.value),
                "compatibleCode" to (compatCode.value?.toInt() ?: 0),
                "url" to (url.value),
                "websiteUrl" to (websiteUrl.value),
                "marketUrl" to (marketUrl.value),
                "content" to (content.value)
            )
        }.await()
        if (submitResult) {
            MyToast.showToast("Successfully Submitted :)")
            ifSuccessful.value = true
        } else {
            MyToast.showToast("Submission Failed :(")
            ifSuccessful.value = false
        }
    }

}