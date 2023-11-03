package app.desty.chat_admin.cloud.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import app.desty.chat_admin.cloud.adapter.CloudConfigListAdapter
import app.desty.chat_admin.cloud.api.CloudApi
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.utils.MyToast
import com.drake.net.Post
import kotlinx.coroutines.CoroutineScope

class CloudMainViewModel : BaseVM() {

    val selectedVersion = MutableLiveData("")
    val searchKey = MutableLiveData("")
    val env = MutableLiveData(Environment.Test)
    val adapter = MutableLiveData(CloudConfigListAdapter())

    fun loadConfigList(callback: (List<CloudConfigInfo>?) -> Unit) {
        scopeNetLife {
            val configList = Post<List<CloudConfigInfo>>("${EnvConfig.getBaseUrl(env.value)}${CloudApi.getConfig}") {
                json(
                    "showAllParams" to true,
                    "showAllVersions" to true
                )
            }.await()
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