package app.desty.chat_admin.common.base

import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.bean.ToolbarConfig
import app.desty.chat_admin.common.utils.LayoutState
import com.blankj.utilcode.util.StringUtils
import com.drake.statelayout.Status
import com.lxj.xpopup.core.BasePopupView

open class BaseVM : ViewModel() {
    var statusBarHeight = MutableLiveData<Int>()
    var statusBarHeightPx = MutableLiveData<Int>()
    var navigationBarHeight = MutableLiveData<Int>()
//    var toolbarConfig: ToolbarConfig? = null
    var toolbarConfig = MutableLiveData<ToolbarConfig>()
    var showHttpLoadingDialog = MutableLiveData<Boolean?>(null)
    var sideTextString = MutableLiveData<String>()
    var showDialog = MutableLiveData<BasePopupView?>(null)
    var showDialogFragment = MutableLiveData<DialogFragment?>(null)
    var finishActivity = MutableLiveData(false)
    var setResult = MutableLiveData(false)
    var intent: Intent? = null

    var httpLoadingDialog =
        LoadingDialogBean(
            false,
            StringUtils.getString(R.string.loading)
                                                                                    )
    var layoutState = LayoutState() {
        when (it) {
            Status.EMPTY -> getEmptyTextRes()
            else         -> null
        }
    }

    class LoadingDialogBean(var cancelAble: Boolean, var msg: String)

    fun showDialog(dialog: BasePopupView?) {
        if (dialog != null) {
            showDialog.value = dialog
        }
    }

    fun showDialog(dialogFragment: DialogFragment?) {
        if (dialogFragment != null) {
            showDialogFragment.value = dialogFragment
        }
    }

    fun setResultFinishActivity(intent: Intent?) {
        this.intent = intent
        setResult.value = true
        finishActivity.value = true
    }

    fun finishActivity() {
        finishActivity.value = true
    }

    protected open fun getEmptyTextRes(): Int = R.string.empty_layout_default
}