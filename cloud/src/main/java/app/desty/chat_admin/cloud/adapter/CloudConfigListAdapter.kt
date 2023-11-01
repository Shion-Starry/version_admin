package app.desty.chat_admin.cloud.adapter

import app.desty.chat_admin.cloud.R
import app.desty.chat_admin.common.bean.CloudConfigInfo
import com.drake.brv.BindingAdapter

class CloudConfigListAdapter : BindingAdapter() {
    private var onEditConfig: ((CloudConfigInfo) -> Unit)? = null
    private var onDeleteConfig: ((CloudConfigInfo) -> Unit)? = null

    fun setEditConfig(block: (CloudConfigInfo) -> Unit): CloudConfigListAdapter {
        onEditConfig = block
        return this
    }

    fun setDeleteConfig(block: (CloudConfigInfo) -> Unit): CloudConfigListAdapter {
        onDeleteConfig = block
        return this
    }

    init {
        addType<CloudConfigInfo>(R.layout.item_cloud_config_list)

        R.id.bbtv_edit.onClick {
            getModelOrNull<CloudConfigInfo>()?.apply {
                onEditConfig?.invoke(this)
            }
        }

        R.id.bbtv_delete.onClick {
            getModelOrNull<CloudConfigInfo>()?.apply {
                onDeleteConfig?.invoke(this)
            }
        }
    }

}