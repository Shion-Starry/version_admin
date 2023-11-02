package app.desty.chat_admin.cloud.pages

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import app.desty.chat_admin.cloud.adapter.CloudConfigListAdapter
import app.desty.chat_admin.cloud.api.CloudApi
import app.desty.chat_admin.common.base.BaseVM
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.config.EnvConfig
import app.desty.chat_admin.common.config.Environment
import com.drake.net.Post

class CloudMainViewModel : BaseVM() {

    val selectedToVersion = MutableLiveData("")
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
            Log.i("The get config request is successful: ", "$configList")
            callback.invoke(configList)
        }.finally {
            callback.invoke(null)
        }
    }

}