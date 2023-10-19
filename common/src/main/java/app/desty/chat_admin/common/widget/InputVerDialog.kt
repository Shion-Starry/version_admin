package app.desty.chat_admin.common.widget

import android.content.Context
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.databinding.DialogInputVerInfoBinding
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView

class InputVerDialog(context: Context) : BottomPopupView(context) {

    var versions: Versions = Versions()
    var title: String = StringUtils.getString(R.string.edit_title_latest_version)
    var okListener: ((VersionGroup) -> Unit)? = null
    private val enableClickOK = MutableLiveData(true)
    private var binding: DialogInputVerInfoBinding? = null

    override fun getImplLayoutId() = R.layout.dialog_input_ver_info

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding?.run {
            setVariable(BR.dialogTitle, title)
            setVariable(BR.versionInput, versions)
            setVariable(BR.ifEnabled, enableClickOK)
            setVariable(BR.click, ClickEvents())
        }
        versions.majorInput.observe(this) { updateInterface() }
        versions.subInput.observe(this) { updateInterface() }
        versions.fixInput.observe(this) { updateInterface() }
        versions.buildInput.observe(this) { updateInterface() }
    }

    private fun updateInterface() {

//        versionInput.majorInput.value =
//            versionInput.majorInput.value?.let {
//                NumberUtil.setInputRangeRules(it,0,999)
//            }
//
//        versionInput.majorInput.value =
//            versionInput.subInput.value?.let {
//                NumberUtil.setInputRangeRules(it,0,99)
//            }
//
//        versionInput.majorInput.value =
//            versionInput.fixInput.value?.let {
//                NumberUtil.setInputRangeRules(it,0,99)
//            }
//
//        versionInput.majorInput.value =
//            versionInput.buildInput.value?.let {
//                NumberUtil.setInputRangeRules(it,0,999)
//            }
        enableClickOK.value = !(versions.majorInput.value.isNullOrBlank()
                || versions.subInput.value.isNullOrBlank()
                || versions.fixInput.value.isNullOrBlank()
                || versions.buildInput.value.isNullOrBlank())

//        Log.d("The current enable state is: ", "${enableClickOK.value}")
    }

    inner class ClickEvents {

        fun clickClose(view: View) {
            dismiss()
        }

        fun clickOk(view: View) {
            okListener?.run {
                val versionGroup = VersionGroup(
                    versions.majorInput.value?.toInt() ?: 0,
                    versions.subInput.value?.toInt() ?: 0,
                    versions.fixInput.value?.toInt() ?: 0,
                    versions.buildInput.value?.toInt() ?: 0
                )
                this(versionGroup)
            }
            dismiss()
        }

    }

}

data class Versions (
    val majorInput: MutableLiveData<String> = MutableLiveData("0"),
    val subInput: MutableLiveData<String> = MutableLiveData("0"),
    val fixInput: MutableLiveData<String> = MutableLiveData("0"),
    val buildInput: MutableLiveData<String> = MutableLiveData("0")
) : BaseObservable() {
    fun setVersions(versionGroup: VersionGroup) {
        majorInput.value = versionGroup.major.toString()
        subInput.value = versionGroup.sub.toString()
        fixInput.value = versionGroup.fix.toString()
        buildInput.value = versionGroup.build.toString()
    }
}