package app.desty.chat_admin.upload.page

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.config.ToolbarClickListener
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import app.desty.chat_admin.upload.databinding.FragmentUploadMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.core.result.navigation
import com.drake.net.utils.scopeLife

@Route(path = RouteConstants.Upload.main)
class UploadMainFragment : BaseVMFragment<UploadMainViewModel>(), ToolbarClickListener {

    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_upload_main, BR.mState, mState)
            .addBindingParam(BR.click, ClickEvents())

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding is FragmentUploadMainBinding) {
            (binding as FragmentUploadMainBinding).apply {
                prlRefresh.onRefresh {
                    mState?.run {
                        scopeLife(block = getVersionInfo()).finally{
                            finish()
                            layoutState.showContent()
                        }
                    }
                }
                mState?.layoutState?.showLoading()
                prlRefresh.setEnableLoadMore(false)
            }
        }
    }

    private val uploadNewResult = ActivityResultCallback<ActivityResult> {
        if (it.resultCode == Activity.RESULT_OK) {
            mState.layoutState.showRefreshing()
        }
    }

    inner class ClickEvents {
        fun clickUploadNew(view: View) {
            ARouter.getInstance()
                .build(RouteConstants.Upload.uploadNew)
                .navigation(this@UploadMainFragment, uploadNewResult)
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
        if (homePageType != HomePageType.Upload) {
            return
        }

        if (buttonType == ToolbarClickListener.RIGHT_OPERATE) {
            mState.layoutState.showRefreshing()
        }

    }

}