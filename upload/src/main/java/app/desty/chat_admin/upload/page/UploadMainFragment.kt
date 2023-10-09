package app.desty.chat_admin.upload.page

import android.os.Bundle
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RouteConstants.Upload.main)
class UploadMainFragment : BaseVMFragment<UploadMainViewModel>() {
    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_upload_main, BR.mState, mState)

    override fun init(savedInstanceState: Bundle?) {

    }

}