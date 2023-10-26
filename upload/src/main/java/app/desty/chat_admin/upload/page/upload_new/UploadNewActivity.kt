package app.desty.chat_admin.upload.page.upload_new

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import app.desty.chat_admin.common.utils.ActivityLifecycleManager
import app.desty.chat_admin.common.utils.MyDialog
import app.desty.chat_admin.common.widget.InputVerDialog
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.drake.net.utils.scopeDialog
import com.lxj.xpopup.XPopup

@Route(path = RouteConstants.Upload.uploadNew)
class UploadNewActivity : BaseVmActivity<UploadNewViewModel>() {
    private val backClick = View.OnClickListener { _: View? ->
        super.onBackPressed()
    }

    override fun init(savedInstanceState: Bundle?) {
        KeyboardUtils.fixAndroidBug5497(this)
    }

    override fun initViewModel() {
        mState.ifSuccessful.observe(this, this::checkSubmitted)
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

    private fun checkSubmitted(success: Boolean) {
        if (success) {
            setResult(RESULT_OK)
            finish()
        }
    }

    inner class ClickEvents {

        fun clickUpload(view: View) {
            if (mState.env.value == Environment.Test) {
                MyDialog.show(ChatAdminDialog.Upload, {
                    scopeDialog(block = mState.uploadNewVer())
                })
            } else if (mState.env.value == Environment.Prod) {
                MyDialog.showOtpDialog {
                    if (it) {
                        scopeDialog(block = mState.uploadNewVer())
                    }
                }
            }
        }

        fun clickLatestVer(view: View) {
            val versionGroup = VersionGroup(mState.featureTextMap["latestCode"]?.value ?: "")
            XPopup.Builder(ActivityLifecycleManager.getInstance().topActivity)
                .asCustom(
                    InputVerDialog(context).apply {
                        title = StringUtils.getString(R.string.edit_title_latest_version)
                        dlgState.setVersions(versionGroup)
                        okListener = {
                            mState.featureTextMap["latestVersion"]?.value = it.getVersionStr()
                            mState.featureTextMap["latestCode"]?.value = it.getVersionCodeStr()
                        }
                    }
                )
                .show()
        }

        fun clickCompatVer(view: View) {
            val versionGroup = VersionGroup(mState.featureTextMap["compatCode"]?.value ?: "")
            XPopup.Builder(ActivityLifecycleManager.getInstance().topActivity)
                .asCustom(
                    InputVerDialog(context).apply {
                        title = StringUtils.getString(R.string.edit_title_compat_version)
                        dlgState.setVersions(versionGroup)
                        okListener = {
                            mState.featureTextMap["compatVersion"]?.value = it.getVersionStr()
                            mState.featureTextMap["compatCode"]?.value = it.getVersionCodeStr()
                        }
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