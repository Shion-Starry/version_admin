package app.desty.chat_admin.upload.page

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.config.ToolbarClickListener
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.HomePageType
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import app.desty.chat_admin.upload.databinding.FragmentUploadMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.net.utils.scopeDialog
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
                        }
                    }
                }.autoRefresh()
                prlRefresh.setEnableLoadMore(false)
            }
        }
    }

    inner class ClickEvents {
        fun clickUploadNew(view: View) {
            ARouter.getInstance()
                .build(RouteConstants.Upload.uploadNew)
                .navigation()
        }
    }

    override fun clickFragToolbar(view: View, homePageType: HomePageType, buttonType: Int) {
        if (homePageType != HomePageType.Upload) {
            return
        }

        if (buttonType == ToolbarClickListener.RIGHT_OPERATE) {
            scopeDialog(block = mState.getVersionInfo())
        }

    }

}