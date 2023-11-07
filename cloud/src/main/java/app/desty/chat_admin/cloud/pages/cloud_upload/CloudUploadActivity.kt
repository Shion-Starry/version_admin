package app.desty.chat_admin.cloud.pages.cloud_upload

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.cloud.BR
import app.desty.chat_admin.cloud.R
import app.desty.chat_admin.common.base.BaseVmActivity
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.bean.CloudConfigInfo
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.config.EditDraft
import app.desty.chat_admin.common.config.Environment
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.enum_bean.ChatAdminDialog
import app.desty.chat_admin.common.utils.MyDialog
import app.desty.chat_admin.common.utils.MyToast
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.drake.net.utils.scopeDialog

@Route(path = RouteConstants.Cloud.upload)
class CloudUploadActivity : BaseVmActivity<CloudUploadViewModel>() {

    @JvmField
    @Autowired(name = "configInfo")
    var passedConfigInfo: CloudConfigInfo? = null

    @JvmField
    @Autowired(name = "environment")
    var passedEnv: Environment? = null

    override fun onBackPressed() {
        if (mState.ifSaveDraft() && mState.editEnabled) {
            MyDialog.show(
                ChatAdminDialog.Draft,
                {
                    mState.saveConfigDraft()
                    super.onBackPressed()
                },
                {
                    mState.clearConfigDraft()
                    super.onBackPressed()
                }
            )
        } else {
            super.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        KeyboardUtils.fixAndroidBug5497(this)

        passedEnv?.apply {
            mState.env = this
        }
        passedConfigInfo?.apply {
            mState.editEnabled = false
            mState.setInitialData(this)
        } ?: also {
            EditDraft.getCloudInfoByEnv(mState.env)?.apply {
                MyDialog.show(
                    ChatAdminDialog.LoadDraft,
                    {
                        mState.setInitialData(this)
                    },
                    {
                        mState.setInitialData(passedConfigInfo)
                        mState.clearConfigDraft()
                    }
                )
            } ?: also {
                mState.setInitialData(passedConfigInfo)
            }
        }
    }

    override fun initViewModel() {
        mState.ifSuccessful.observe(this, this::checkSubmitted)
    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.activity_cloud_upload, BR.mState, mState)
            .addBindingParam(BR.click, ClickEvents())

    override fun getToolbarConfig(): ToolbarConfig {
        return ToolbarConfig(
            titleTextBold = true,
            title = StringUtils.getString(R.string.upload_cloud_toolbar_title),
            showBack = true,
            backClick = { onBackPressed() }
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
            if (mState.checkFormat()) {
                if (mState.env == Environment.Test) {
                    MyDialog.show(
                        ChatAdminDialog.Upload,
                        { scopeDialog(block = mState.uploadCloud()) }
                    )
                } else if (mState.env == Environment.Prod) {
                    MyDialog.showOtpDialog {
                        if (it) {
                            scopeDialog(block = mState.uploadCloud())
                        }
                    }
                }
            } else {
                MyToast.showToast("The To Version cannot be earlier than the From Version :(")
            }
        }

        fun clickFromVer(view: View) {
            val versionGroup = VersionGroup(mState.fromVersion.value ?: "")
            MyDialog.showInputVerDialog(
                StringUtils.getString(R.string.edit_title_from_which_version),
                versionGroup
            ) {
                mState.fromVersion.value = it.getVersionCodeStr()
            }
        }

        fun clickToVer(view: View) {
            val versionGroup = VersionGroup(mState.toVersion.value ?: "")
            MyDialog.showInputVerDialog(
                StringUtils.getString(R.string.edit_title_to_which_version),
                versionGroup
            ) {
                mState.toVersion.value = it.getVersionCodeStr()
            }
        }

    }

}