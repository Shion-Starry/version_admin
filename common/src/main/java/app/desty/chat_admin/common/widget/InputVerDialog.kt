package app.desty.chat_admin.common.widget

import android.content.Context
import android.text.Editable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.databinding.DialogInputVerInfoBinding
import app.desty.chat_admin.common.utils.NumberUtil
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView

class InputVerDialog(context: Context) : BottomPopupView(context) {

    var title: String = StringUtils.getString(R.string.edit_title_latest_version)
    var okListener: ((VersionGroup) -> Unit)? = null
    var dlgState: DialogState = DialogState()
    private var binding: DialogInputVerInfoBinding? = null

    override fun getImplLayoutId() = R.layout.dialog_input_ver_info

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.lifecycleOwner = this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding?.run {
            setVariable(BR.dialogTitle, title)
            setVariable(BR.dState, dlgState)
            setVariable(BR.proxy, ProxyEvents())
        }

        dlgState.ifEnabled.addSource(dlgState.versions.majorInput) { updateInterface() }
        dlgState.ifEnabled.addSource(dlgState.versions.subInput) { updateInterface() }
        dlgState.ifEnabled.addSource(dlgState.versions.fixInput) { updateInterface() }
        dlgState.ifEnabled.addSource(dlgState.versions.buildInput) { updateInterface() }

    }

    private fun updateInterface() {
        dlgState.ifEnabled.value = !(dlgState.versions.majorInput.value.isNullOrBlank()
                || dlgState.versions.subInput.value.isNullOrBlank()
                || dlgState.versions.fixInput.value.isNullOrBlank()
                || dlgState.versions.buildInput.value.isNullOrBlank())
    }

    inner class ProxyEvents {
        fun clickClose(view: View) {
            dismiss()
        }

        fun clickOk(view: View) {
            okListener?.run {
                val versionGroup = VersionGroup(
                    dlgState.versions.majorInput.value?.toLong() ?: 0,
                    dlgState.versions.subInput.value?.toLong() ?: 0,
                    dlgState.versions.fixInput.value?.toLong() ?: 0,
                    dlgState.versions.buildInput.value?.toLong() ?: 0
                )
                this(versionGroup)
            }
            dismiss()
        }

        fun getTextWatcher(type:Int, editable: Editable) {
            when (type) {
                1 -> { NumberUtil.setInputRangeRules(editable, 0, 999) }
                2 -> { NumberUtil.setInputRangeRules(editable, 0, 99) }
                3 -> { NumberUtil.setInputRangeRules(editable, 0, 99) }
                4 -> { NumberUtil.setInputRangeRules(editable, 0, 999) }
            }
        }
    }

}

data class DialogState(
    val versions: Versions = Versions(),
    val ifEnabled : MediatorLiveData<Boolean> = MediatorLiveData(true)
) {
    fun setVersions(versionGroup: VersionGroup) {
        versions.majorInput.value = versionGroup.major.toString()
        versions.subInput.value = versionGroup.sub.toString()
        versions.fixInput.value = versionGroup.fix.toString()
        versions.buildInput.value = versionGroup.build.toString()
    }

    fun getSpecified(type: Int): MutableLiveData<String> {
        return when (type) {
            1 -> versions.majorInput
            2 -> versions.subInput
            3 -> versions.fixInput
            4 -> versions.buildInput
            else -> MutableLiveData("")
        }
    }

    fun getSubTitle(type: Int): String {
        return when (type) {
            1 -> StringUtils.getString(R.string.edit_subtitle_version_setting)
            2 -> StringUtils.getString(R.string.edit_subtitle_sub_version)
            3 -> StringUtils.getString(R.string.edit_subtitle_fix_version)
            4 -> StringUtils.getString(R.string.edit_subtitle_build_version)
            else -> ""
        }
    }
}

data class Versions (
    val majorInput: MutableLiveData<String> = MutableLiveData("0"),
    val subInput: MutableLiveData<String> = MutableLiveData("0"),
    val fixInput: MutableLiveData<String> = MutableLiveData("0"),
    val buildInput: MutableLiveData<String> = MutableLiveData("0")
)