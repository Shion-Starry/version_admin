package app.desty.chat_admin.cloud.pages.cloud_upload

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.cloud.api.CloudApi
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.config.EditDraft
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.model.gson
import app.desty.chat_admin.common.utils.MyToast
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class CloudUploadViewModel : BaseVM() {

    private val configName = MutableLiveData("")
    private val configValue = MutableLiveData("")
    val fromVersion = MutableLiveData("")
    val toVersion = MutableLiveData("")
    private val uniqueId = MutableLiveData("")
    private val channel = MutableLiveData("")
    private val infoList = listOf(
        configName,
        configValue,
        fromVersion,
        toVersion,
        uniqueId,
        channel
    )
    val canUpload = MediatorLiveData(false)
    var env = Environment.Test
    val ifSuccessful = MutableLiveData(false)
    var editEnabled = true
    private var presetConfig: CloudConfigInfo? = null

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

    fun setEditEnabled(key: String): Boolean {
        return when (key) {
            "Unique ID"    -> editEnabled
            else               -> true
        }
    }

    fun ifSaveDraft(): Boolean = buildConfigInfo() != presetConfig

    fun saveConfigDraft() {
        EditDraft.setCloudInfoByEnv(env, buildConfigInfo())
    }

    fun clearConfigDraft() {
        EditDraft.setCloudInfoByEnv(env, null)
    }

    fun setInitialData(cloudConfigInfo: CloudConfigInfo?) {
        presetConfig = cloudConfigInfo?.also {
            configName.value = it.name
            configValue.value = it.value
            fromVersion.value = VersionGroup(it.fromVersionCode).getVersionCodeStr()
            toVersion.value = VersionGroup(it.toVersionCode).getVersionCodeStr()
            uniqueId.value = it.uniqueKey
            channel.value = it.channel
        } ?: buildConfigInfo()
    }

    fun getSpecific(key: String): MutableLiveData<String> {
        return when (key) {
            "Config Name" -> configName
            "Setting" -> configValue
            "From Which Version" -> fromVersion
            "To Which Version" -> toVersion
            "Unique ID" -> uniqueId
            "Channel" -> channel
            else -> MutableLiveData("")
        }
    }

    private fun buildConfigInfo() = CloudConfigInfo(
        name = configName.value ?: "",
        value = configValue.value ?: "",
        fromVersionCode = fromVersion.value?.toIntOrNull() ?: 0,
        toVersionCode = toVersion.value?.toIntOrNull() ?: 0,
        uniqueKey = uniqueId.value ?: "",
        channel = channel.value ?: ""
    )

    fun uploadCloud(): suspend CoroutineScope.() -> Unit = {
        Post<String>("${EnvConfig.getBaseUrl(env)}${CloudApi.saveConfig}") {
            gson(
                mapOf(
                    "saveList" to listOf(
                        buildConfigInfo()
                    )
                )
            )
        }.await()
        clearConfigDraft()
        MyToast.showToast("Successfully Submitted :)")
        ifSuccessful.value = true
    }

}