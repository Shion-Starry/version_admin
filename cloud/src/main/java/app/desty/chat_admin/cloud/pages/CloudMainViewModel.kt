package app.desty.chat_admin.cloud.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import app.desty.chat_admin.cloud.adapter.CloudConfigListAdapter
import app.desty.chat_admin.cloud.api.CloudApi
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.utils.ConfigFilter
import app.desty.chat_admin.common.utils.ConfigFilterUtils
import app.desty.chat_admin.common.utils.MyToast
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class CloudMainViewModel : BaseVM() {

    val selectedVersion = MutableLiveData("")
    val searchKey = MutableLiveData("")
    val env = MutableLiveData(Environment.Test)
    private val cachedData = MutableLiveData<List<CloudConfigInfo>>()
    val adapter = MutableLiveData(CloudConfigListAdapter())

    private val customizedFilter = object : ConfigFilter {
        override fun filter(
            configInfoList: List<CloudConfigInfo>,
            configInfo: CloudConfigInfo
        ): Boolean {
            selectedVersion.value?.apply {
                toIntOrNull()?.let {
                    if (configInfo.fromVersionCode > it || configInfo.toVersionCode < it) {
                        return false
                    }
                }
            }
            searchKey.value?.apply {
                if (isNotBlank()) {
                    if (configInfo.name != trim()) {
                        return false
                    }
                }
            }
            return true
        }
    }

    fun updateDisplay() {
        cachedData.value?.apply {
            val models = filterConfigInfoList(this)
            adapter.value?.models = models
            if (models.isNotEmpty()) {
                layoutState.showContent()
            } else {
                layoutState.showEmpty()
            }
        } ?: also {
            layoutState.showRefreshing()
        }
    }

    fun filterConfigInfoList(configInfoList: List<CloudConfigInfo>): List<CloudConfigInfo> {
        return ConfigFilterUtils.getConfigInfoListByFilter(
            configInfoList,
            customizedFilter
        )
    }

    fun loadConfigList(callback: (List<CloudConfigInfo>?) -> Unit) {
        scopeNetLife {
            val configList = Post<List<CloudConfigInfo>>("${EnvConfig.getBaseUrl(env.value)}${CloudApi.getConfig}") {
                json(
                    "showAllParams" to true,
                    "showAllVersions" to true
                )
            }.await()
            cachedData.value = configList
            callback.invoke(configList)
        }.finally {
            callback.invoke(null)
        }
    }

    fun deleteConfig(ccInfo: CloudConfigInfo): suspend CoroutineScope.() -> Unit = {
        Post<String>("${EnvConfig.getBaseUrl(env.value)}${CloudApi.deleteConfig}") {
            json(
                "uniqueKey" to ccInfo.uniqueKey
            )
        }.await()
        MyToast.showToast("Successfully Deleted :)")
    }

}