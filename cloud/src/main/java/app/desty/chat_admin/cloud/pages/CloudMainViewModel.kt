package app.desty.chat_admin.cloud.pages

import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.cloud.adapter.CloudConfigListAdapter
import app.desty.chat_admin.common.base.BaseVM

class CloudMainViewModel : BaseVM() {

    val adapter = MutableLiveData(CloudConfigListAdapter())

}