package app.desty.chat_admin.common.utils

import app.desty.chat_admin.common.bean.CloudConfigInfo

object ConfigFilterUtils {
    fun getConfigInfoListByFilter(
        configInfoList: List<CloudConfigInfo>,
        configFilter: ConfigFilter
    ): List<CloudConfigInfo> {

        val filteredList = mutableListOf<CloudConfigInfo>()
        configInfoList.forEach {
            if (configFilter.filter(configInfoList, it)) {
                filteredList.add(it)
            }
        }
        return filteredList
    }

}

interface ConfigFilter {
    fun filter(configInfoList: List<CloudConfigInfo>, configInfo: CloudConfigInfo): Boolean
}