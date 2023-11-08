package app.desty.chat_admin.common.widget

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.VersionGroup
import app.desty.chat_admin.common.databinding.DialogVersionPickerBinding
import app.desty.chat_admin.common.utils.FontUtil
import app.desty.sdk.picker.wheelview.annotation.ScrollState
import app.desty.sdk.picker.wheelview.contract.OnWheelChangedListener
import app.desty.sdk.picker.wheelview.widget.WheelView
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.core.BottomPopupView

class VersionPickerDialog(context: Context) : BottomPopupView(context) {

    var title: String = StringUtils.getString(R.string.edit_title_latest_version)
    private val ifEnabled = MutableLiveData(true)
    var versionGroup = VersionGroup()
    var okListener: ((VersionGroup) -> Unit)? = null
    private var binding: DialogVersionPickerBinding? = null

    private val majorList: MutableList<String> = mutableListOf()
    private val buildList: MutableList<String> = mutableListOf()
    private val otherList: MutableList<String> = mutableListOf()

    init {
        for (i in 0..210) {
            majorList.add(String.format("%d", i))
        }

        for (i in 0..999) {
            buildList.add(String.format("%03d", i))
        }

        for (i in 0..99) {
            otherList.add(String.format("%02d", i))
        }
    }

    // 用于控制确认按钮是否可用的阀门
    companion object {
        private const val majorValve = 1
        private const val subValve = 1 shl 1
        private const val fixValve = 1 shl 2
        private const val buildValve = 1 shl 3
        private const val fullyOpen = majorValve or subValve or fixValve or buildValve
    }

    private var valveState = fullyOpen

    override fun getImplLayoutId() = R.layout.dialog_version_picker

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.lifecycleOwner = this
        binding?.run {
            major.typeface = FontUtil.getFontTypefaceByName("font/regular_font.ttf")
            sub.typeface = FontUtil.getFontTypefaceByName("font/regular_font.ttf")
            fix.typeface = FontUtil.getFontTypefaceByName("font/regular_font.ttf")
            build.typeface = FontUtil.getFontTypefaceByName("font/regular_font.ttf")

            major.data = majorList
            major.visibleItemCount = 5
            major.isCyclicEnabled = true
            major.setDefaultPosition(versionGroup.major)
            major.setOnWheelChangedListener(
                object : OnWheelChangedListener {
                    override fun onWheelScrolled(p0: WheelView?, p1: Int) {
                    }

                    override fun onWheelSelected(p0: WheelView?, p1: Int) {
                        if (p1 == 210) {
                            sub.smoothScrollTo(0)
                            fix.smoothScrollTo(0)
                            build.smoothScrollTo(0)
                        }
                    }

                    override fun onWheelScrollStateChanged(p0: WheelView?, p1: Int) {
                        valveState = when (p1) {
                            ScrollState.IDLE             -> valveState or majorValve
                            ScrollState.SCROLLING    -> valveState and majorValve.inv()
                            ScrollState.DRAGGING     -> valveState and majorValve.inv()
                            else                              -> valveState and majorValve.inv()
                        }
                        ifEnabled.value = valveState == fullyOpen
                    }

                    override fun onWheelLoopFinished(p0: WheelView?) {
                    }
                }
            )

            sub.data = otherList
            sub.visibleItemCount = 5
            sub.isCyclicEnabled = true
            sub.setDefaultPosition(versionGroup.sub)
            sub.setOnWheelChangedListener(
                object : OnWheelChangedListener {
                    override fun onWheelScrolled(p0: WheelView?, p1: Int) {
                    }

                    override fun onWheelSelected(p0: WheelView?, p1: Int) {
                        if (major.currentPosition == 210) {
                            sub.smoothScrollTo(0)
                        }
                    }

                    override fun onWheelScrollStateChanged(p0: WheelView?, p1: Int) {
                        valveState = when (p1) {
                            ScrollState.IDLE             -> valveState or subValve
                            ScrollState.SCROLLING    -> valveState and subValve.inv()
                            ScrollState.DRAGGING     -> valveState and subValve.inv()
                            else                              -> valveState and subValve.inv()
                        }
                        ifEnabled.value = valveState == fullyOpen
                    }

                    override fun onWheelLoopFinished(p0: WheelView?) {
                    }
                }
            )

            fix.data = otherList
            fix.visibleItemCount = 5
            fix.isCyclicEnabled = true
            fix.setDefaultPosition(versionGroup.fix)
            fix.setOnWheelChangedListener(
                object : OnWheelChangedListener {
                    override fun onWheelScrolled(p0: WheelView?, p1: Int) {
                    }

                    override fun onWheelSelected(p0: WheelView?, p1: Int) {
                        if (major.currentPosition == 210) {
                            fix.smoothScrollTo(0)
                        }
                    }

                    override fun onWheelScrollStateChanged(p0: WheelView?, p1: Int) {
                        valveState = when (p1) {
                            ScrollState.IDLE             -> valveState or fixValve
                            ScrollState.SCROLLING    -> valveState and fixValve.inv()
                            ScrollState.DRAGGING     -> valveState and fixValve.inv()
                            else                              -> valveState and fixValve.inv()
                        }
                        ifEnabled.value = valveState == fullyOpen
                    }

                    override fun onWheelLoopFinished(p0: WheelView?) {
                    }
                }
            )

            build.data = buildList
            build.visibleItemCount = 5
            build.isCyclicEnabled = true
            build.setDefaultPosition(versionGroup.build)
            build.setOnWheelChangedListener(
                object : OnWheelChangedListener {
                    override fun onWheelScrolled(p0: WheelView?, p1: Int) {
                    }

                    override fun onWheelSelected(p0: WheelView?, p1: Int) {
                        if (major.currentPosition == 210) {
                            build.smoothScrollTo(0)
                        }
                    }

                    override fun onWheelScrollStateChanged(p0: WheelView?, p1: Int) {
                        valveState = when (p1) {
                            ScrollState.IDLE             -> valveState or buildValve
                            ScrollState.SCROLLING    -> valveState and buildValve.inv()
                            ScrollState.DRAGGING     -> valveState and buildValve.inv()
                            else                              -> valveState and buildValve.inv()
                        }
                        ifEnabled.value = valveState == fullyOpen
                    }

                    override fun onWheelLoopFinished(p0: WheelView?) {
                    }
                }
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding?.run {
            setVariable(BR.dialogTitle, title)
            setVariable(BR.isEnabled, ifEnabled)
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
                        majorList[it.major.currentPosition].toIntOrNull() ?: 0,
                        otherList[it.sub.currentPosition].toIntOrNull() ?: 0,
                        otherList[it.fix.currentPosition].toIntOrNull() ?: 0,
                        buildList[it.build.currentPosition].toIntOrNull() ?: 0
                    )
                    this(versionGroup)
                }
            }
            dismiss()
        }
    }

}