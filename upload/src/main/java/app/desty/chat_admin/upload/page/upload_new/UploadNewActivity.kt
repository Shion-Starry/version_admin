package app.desty.chat_admin.upload.page.upload_new

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.bean.VersionInfo
import app.desty.chat_admin.common.config.EditVersionDraft
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import app.desty.chat_admin.common.utils.MyDialog
import app.desty.chat_admin.upload.BR
import app.desty.chat_admin.upload.R
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.drake.net.utils.scopeDialog

@Route(path = RouteConstants.Upload.uploadNew)
class UploadNewActivity : BaseVmActivity<UploadNewViewModel>() {

    @JvmField
    @Autowired(name = "verInfo")
    var passedVerInfo: VersionInfo? = null

    override fun onBackPressed() {
        if (mState.ifSaveDraft()) {
            MyDialog.show(
                ChatAdminDialog.Draft,
                {
                    mState.saveVerDraft()
                    super.onBackPressed()
                },
                {
                    mState.clearVerDraft()
                    super.onBackPressed()
                }
            )
        } else {
            super.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        passedVerInfo?.apply {
            content = ""
        }
        KeyboardUtils.fixAndroidBug5497(this)
        if (EditVersionDraft.verInfo == null) {
            mState.setInitialData(passedVerInfo)
        } else {
            MyDialog.show(
                ChatAdminDialog.LoadDraft,
                {
                    mState.setInitialData(EditVersionDraft.verInfo)
                },
                {
                    mState.setInitialData(passedVerInfo)
                    mState.clearVerDraft()
                }
            )
        }
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
            title = StringUtils.getString(R.string.upload_ver_toolbar_title),
            showBack = true,
            backClick = {onBackPressed()}
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
            val versionGroup = VersionGroup(mState.latestCode.value ?: "")
            MyDialog.showInputVerDialog(
                StringUtils.getString(R.string.edit_title_latest_version),
                versionGroup
            ) {
                mState.latestVersion.value = it.getVersionStr()
                mState.latestCode.value = it.getVersionCodeStr()
            }
        }

        fun clickCompatVer(view: View) {
            val versionGroup = VersionGroup(mState.compatCode.value ?: "")
            MyDialog.showInputVerDialog(
                StringUtils.getString(R.string.edit_title_compat_version),
                versionGroup
            ) {
                mState.compatVersion.value = it.getVersionStr()
                mState.compatCode.value = it.getVersionCodeStr()
            }
        }

        fun clickFirstOp(view: View) {
            mState.env.value = Environment.Test
        }

        fun clickSecondOp(view: View) {
            mState.env.value = Environment.Prod
        }

    }

}