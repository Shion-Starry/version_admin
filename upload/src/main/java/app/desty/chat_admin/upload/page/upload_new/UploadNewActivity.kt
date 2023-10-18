package app.desty.chat_admin.upload.page.upload_new

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.utils.ActivityLifecycleManager
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.common.widget.InputVerDialog
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.XPopup

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
            .addBindingParam(BR.click, ClickEvents())

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

        fun clickLatestVer(view: View) {
            MyToast.showToast("This is to edit the Latest Version")
        }

        fun clickCompatVer(view: View) {
            XPopup.Builder(ActivityLifecycleManager.getInstance().topActivity)
                .asCustom(
                    InputVerDialog(context).apply {
                    title = StringUtils.getString(R.string.edit_title_compat_version)
                    }
                )
                .show()
        }

        fun clickFirstOp(view: View) {
            mState.env.value = Environment.Test
        }

        fun clickSecondOp(view: View) {
            mState.env.value = Environment.Prod
        }

    }

}