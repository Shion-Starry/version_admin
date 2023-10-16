package app.desty.chat_admin.common.widget

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.EditorSetting
import app.desty.chat_admin.common.databinding.DialogInputVerInfoBinding
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView

class InputVerDialog : BottomPopupView {

    private var binding: DialogInputVerInfoBinding? = null
    private var editorTextList = mutableListOf<MutableLiveData<String>>()
    private var title = StringUtils.getString(R.string.edit_title_latest_version)
    private var subTitle = StringUtils.getString(R.string.edit_subtitle_version_setting)
    private var inputType = 0
    private var maxValueList = mutableListOf<Int>()
    private var minValueList = mutableListOf<Int>()

    constructor(context: Context) : super(context)

    constructor(context: Context, editorSetting: EditorSetting) : super(context) {
        this.title = editorSetting.getFinalTitle()
        this.subTitle = editorSetting.getFinalSubTitle()
        this.inputType = editorSetting.inputType
        this.maxValueList = editorSetting.maxValueList
        this.minValueList = editorSetting.minValueList
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_input_ver_info
    }

    override fun onCreate() {
        binding = DataBindingUtil.bind(
            popupImplView
        )
        super.onCreate()
    }



}