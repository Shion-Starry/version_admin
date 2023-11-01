package app.desty.chat_admin.upload.page.upload_new

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.VersionInfo
import app.desty.chat_admin.common.config.EditVersionDraft
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.model.gson
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.upload.api.UploadApi
import com.blankj.utilcode.util.KsonUtils
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class UploadNewViewModel : BaseVM() {

    private val channel = MutableLiveData("")
    val latestVersion = MutableLiveData("")
    val latestCode = MutableLiveData("")
    val compatVersion = MutableLiveData("")
    val compatCode = MutableLiveData("")
    private val url = MutableLiveData("")
    private val websiteUrl = MutableLiveData("")
    private val marketUrl = MutableLiveData("")
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
    private var presetVer: VersionInfo? = null

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

    fun ifSaveDraft(): Boolean = buildVerInfo() != presetVer

    fun saveVerDraft() {
        EditVersionDraft.verInfo = buildVerInfo()
    }

    fun clearVerDraft() {
        EditVersionDraft.verInfo = null
    }

    fun setInitialData(versionInfo: VersionInfo?) {
        presetVer = versionInfo?.also {
            channel.value = it.channel
            latestVersion.value = it.latestVersion
            latestCode.value = it.latestCode.toString()
            compatVersion.value = it.compatVersion
            compatCode.value = it.compatCode.toString()
            url.value = it.url
            websiteUrl.value = it.websiteUrl
            marketUrl.value = it.marketUrl
            content.value = it.content
        }
    }

    fun getSpecific(key: String): MutableLiveData<String> {
        return when (key) {
            "Channel"             -> channel
            "URL"                  -> url
            "Website URL"       -> websiteUrl
            "Market URL"        -> marketUrl
            "Update Content"  -> content
            else                     -> MutableLiveData("")
        }
    }

    private fun buildVerInfo() = VersionInfo(
        channel = channel.value ?: "",
        latestVersion = latestVersion.value ?: "",
        latestCode = latestCode.value?.toIntOrNull() ?: 0,
        compatVersion = compatVersion.value ?: "",
        compatCode = compatCode.value?.toIntOrNull() ?: 0,
        url = url.value ?: "",
        websiteUrl = websiteUrl.value ?: "",
        marketUrl = marketUrl.value ?: "",
        content = content.value ?: ""
    )

    fun uploadNewVer(): suspend CoroutineScope.() -> Unit = {
        Post<Boolean>("${EnvConfig.getBaseUrl(env.value)}${UploadApi.saveVersion}") {
            KsonUtils
            gson(
                buildVerInfo()
            )
        }.await()
        clearVerDraft()
        MyToast.showToast("Successfully Submitted :)")
        ifSuccessful.value = true
    }

}