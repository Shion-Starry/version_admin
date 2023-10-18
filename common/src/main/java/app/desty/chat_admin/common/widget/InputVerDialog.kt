package app.desty.chat_admin.common.widget

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.databinding.DialogInputVerInfoBinding
import app.desty.chat_admin.common.utils.NumberUtil
import app.desty.sdk.logcat.Logcat
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView

class InputVerDialog(context: Context) : BottomPopupView(context) {

    var versionInput: VersionInput = VersionInput
    var title: String = StringUtils.getString(R.string.edit_title_latest_version)
    var okListener: ((VersionGroup?) -> Unit)? = null
    private val enableClickOK = MediatorLiveData(true)
    private var binding: DialogInputVerInfoBinding? = null

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_input_ver_info
    }

    override fun onCreate() {
        binding = DataBindingUtil.bind(
            popupImplView
        )
        super.onCreate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        enableClickOK.addSource(versionInput.majorInput) { updateInterface() }
        enableClickOK.addSource(versionInput.subInput) { updateInterface() }
        enableClickOK.addSource(versionInput.fixInput) { updateInterface() }
        enableClickOK.addSource(versionInput.buildInput) { updateInterface() }
        binding?.run {
            this.dialogTitle = title
            this.versions = versionInput
            this.ifEnabled = enableClickOK
            this.click = ClickEvents()
        }
    }

    private fun updateInterface() {

        Logcat.i("Some of the Live Data is changed")

        versionInput.majorInput.value =
            versionInput.majorInput.value?.let {
                NumberUtil.setInputRangeRules(it,0,999)
            }

        versionInput.majorInput.value =
            versionInput.subInput.value?.let {
                NumberUtil.setInputRangeRules(it,0,99)
            }

        versionInput.majorInput.value =
            versionInput.fixInput.value?.let {
                NumberUtil.setInputRangeRules(it,0,99)
            }

        versionInput.majorInput.value =
            versionInput.buildInput.value?.let {
                NumberUtil.setInputRangeRules(it,0,999)
            }

        enableClickOK.value = !(versionInput.majorInput.value.isNullOrBlank()
                || versionInput.subInput.value.isNullOrBlank()
                || versionInput.fixInput.value.isNullOrBlank()
                || versionInput.buildInput.value.isNullOrBlank())
    }

    inner class ClickEvents {

        fun clickClose(view: View) {
            dismiss()
        }

        fun clickOk(view: View) {
            okListener?.run {
                val versionGroup = VersionGroup(
                    versionInput.majorInput.value?.toInt() ?: 0,
                    versionInput.subInput.value?.toInt() ?: 0,
                    versionInput.fixInput.value?.toInt() ?: 0,
                    versionInput.buildInput.value?.toInt() ?: 0
                )
                this(versionGroup)
            }
            dismiss()
        }

    }

}

object VersionInput {
    val majorInput: MutableLiveData<String> = MutableLiveData("0")
    val subInput: MutableLiveData<String> = MutableLiveData("0")
    val fixInput: MutableLiveData<String> = MutableLiveData("0")
    val buildInput: MutableLiveData<String> = MutableLiveData("0")
}