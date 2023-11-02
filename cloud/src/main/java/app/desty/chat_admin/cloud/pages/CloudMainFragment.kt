package app.desty.chat_admin.cloud.pages

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import app.desty.chat_admin.cloud.BR
import app.desty.chat_admin.cloud.R
import app.desty.chat_admin.cloud.databinding.FragmentCloudMainBinding
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.config.ToolbarClickListener
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.common.utils.MyToast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.core.result.navigation

@Route(path = RouteConstants.Cloud.main)
class CloudMainFragment : BaseVMFragment<CloudMainViewModel>(), ToolbarClickListener {
    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_cloud_main, BR.mState, mState)
            .addBindingParam(BR.click, ClickEvents())

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mState.adapter.value?.setEditConfig(this.ClickEvents()::editConfig)
            ?.setDeleteConfig(this.ClickEvents()::deleteConfig)

        if (binding is FragmentCloudMainBinding) {
            (binding as FragmentCloudMainBinding).apply {
                prlConfigs.onRefresh {
                    mState?.loadConfigList {
                        val models = mutableListOf<CloudConfigInfo>()
                        it?.apply {
                            this.forEach { item ->
                                models += item
                            }
                            addData(models)
                        }
                        finish()
                    }
                }.autoRefresh()
                prlConfigs.onLoadMore {
                    mState?.loadConfigList {
                        val models = mutableListOf<CloudConfigInfo>()
                        it?.apply {
                            this.forEach { item ->
                                models += item
                            }
                            addData(models)
                        }
                        finish()
                    }
                }
            }
        }

    }

    private val uploadCloudResult = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == Activity.RESULT_OK) {
            MyToast.showToast("The draft has been saved :)")
        }

    }

    inner class ClickEvents {
        fun editConfig(ccInfo: CloudConfigInfo) {
            MyToast.showToast("The config to be edited: $ccInfo")
//            ARouter.getInstance()
//                .build(RouteConstants.Cloud.upload)
//                .navigation(this@CloudMainFragment, uploadCloudResult)
        }

        fun deleteConfig(ccInfo: CloudConfigInfo) {
            MyToast.showToast("The config to be deleted: $ccInfo")
//            ARouter.getInstance()
//                .build(RouteConstants.Cloud.upload)
//                .navigation(this@CloudMainFragment, uploadCloudResult)
        }

        fun clearSearchBox(view: View) {
            mState.searchKey.value = ""
        }

        fun clearFilter(view: View) {
            mState.selectedToVersion.value = ""
        }

        fun clickFirstOp(view: View) {
            mState.env.value = Environment.Test
            mState.layoutState.showRefreshing()
        }

        fun clickSecondOp(view: View) {
            mState.env.value = Environment.Prod
            mState.layoutState.showRefreshing()
        }

    }

    override fun clickFragToolbar(view: View, homePageType: HomePageType, buttonType: Int) {
        if (homePageType != HomePageType.Cloud) {
            return
        }

        if (buttonType == ToolbarClickListener.RIGHT_OPERATE) {
            ARouter.getInstance()
                .build(RouteConstants.Cloud.upload)
                .navigation(this@CloudMainFragment, uploadCloudResult)
        }
    }

}