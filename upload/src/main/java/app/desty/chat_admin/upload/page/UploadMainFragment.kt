package app.desty.chat_admin.upload.page

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = RouteConstants.Upload.main)
class UploadMainFragment : BaseVMFragment<UploadMainViewModel>() {
    override fun initViewModel() {
        mState.getVersionInfo()
    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_upload_main, BR.mState, mState)
            .addBindingParam(BR.click, ClickEvents())

    override fun init(savedInstanceState: Bundle?) {

    }

    inner class ClickEvents {
        fun clickUploadNew(view: View) {
            ARouter.getInstance()
                .build(RouteConstants.Upload.uploadNew)
                .navigation()
        }
    }

}