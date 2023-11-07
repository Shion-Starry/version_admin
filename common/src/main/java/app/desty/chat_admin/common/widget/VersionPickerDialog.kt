package app.desty.chat_admin.common.widget

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.databinding.DialogVersionPickerBinding
import app.desty.chat_admin.common.utils.FontUtil
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopupext.adapter.NumericWheelAdapter

class VersionPickerDialog(context: Context) : BottomPopupView(context) {

    var title: String = StringUtils.getString(R.string.edit_title_latest_version)
    var versionGroup = VersionGroup()
    var okListener: ((VersionGroup) -> Unit)? = null
    private var binding: DialogVersionPickerBinding? = null

    override fun getImplLayoutId() = R.layout.dialog_version_picker

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.lifecycleOwner = this
        binding?.run {
            major.setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
            sub.setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
            fix.setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))
            build.setTypeface(FontUtil.getFontTypefaceByName("font/regular_font.ttf"))

            major.adapter = NumericWheelAdapter(0, 210)
            major.setItemsVisibleCount(5)
            major.currentItem = versionGroup.major
            major.setOnItemSelectedListener {
                if (it == 210) {
                    sub.currentItem = 0
                    fix.currentItem = 0
                    build.currentItem = 0
                }
            }

            sub.adapter = NumericWheelAdapter(0, 99)
            sub.setItemsVisibleCount(5)
            sub.currentItem = versionGroup.sub
            sub.setOnItemSelectedListener {
                if (major.currentItem == 210) {
                    sub.currentItem = 0
                }
            }

            fix.adapter = NumericWheelAdapter(0, 99)
            fix.setItemsVisibleCount(5)
            fix.currentItem = versionGroup.fix
            fix.setOnItemSelectedListener {
                if (major.currentItem == 210) {
                    fix.currentItem = 0
                }
            }

            build.adapter = NumericWheelAdapter(0, 999)
            build.setItemsVisibleCount(5)
            build.currentItem = versionGroup.build
            build.setOnItemSelectedListener {
                if (major.currentItem == 210) {
                    build.currentItem = 0
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding?.run {
            setVariable(BR.dialogTitle, title)
            setVariable(BR.proxy, ProxyEvents())
        }
    }

    inner class ProxyEvents {
        fun clickClose(view: View) {
            dismiss()
        }

        fun clickOk(view: View) {
            okListener?.run {
                binding?.let {
                    val versionGroup = VersionGroup(
                        it.major.currentItem,
                        it.sub.currentItem,
                        it.fix.currentItem,
                        it.build.currentItem
                    )
                    this(versionGroup)
                }
            }
            dismiss()
        }
    }

}