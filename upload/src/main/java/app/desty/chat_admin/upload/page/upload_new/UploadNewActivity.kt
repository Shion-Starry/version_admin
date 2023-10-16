package app.desty.chat_admin.upload.page.upload_new

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RouteConstants.Upload.uploadNew)
class UploadNewActivity : BaseVmActivity<UploadNewViewModel>() {
    private val backClick = View.OnClickListener { _: View? ->
        super.onBackPressed()
    }
    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.activity_upload_new, BR.mState, mState)

    override fun getToolbarConfig(): ToolbarConfig {
        return ToolbarConfig(
            titleTextBold = true,
            title = getString(R.string.upload_ver_toolbar_title),
            showBack = true,
            backClick = backClick
        )
    }

    inner class ClickEvents {
        fun clickUpload(view: View) {

        }

    }

}