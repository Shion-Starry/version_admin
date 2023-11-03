package app.desty.chat_admin.common.widget

import android.content.Context
import android.text.Editable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import app.desty.chat.otp.OtpUtils
import app.desty.chat_admin.common.BR
import app.desty.chat_admin.common.R
import app.desty.chat_admin.common.databinding.DialogOtpVerificationBinding
import app.desty.chat_admin.common.utils.MyToast
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.lxj.xpopup.core.CenterPopupView

class OtpVerifyDialog(context: Context) : CenterPopupView(context) {

    var confirmListener: ((Boolean) -> Unit)? = null
    private var authCodes: AuthCodeList = AuthCodeList()
    private var binding: DialogOtpVerificationBinding? = null

    override fun getImplLayoutId() = R.layout.dialog_otp_verification

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.lifecycleOwner = this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding?.run {
            setVariable(BR.codeList, authCodes)
            setVariable(BR.proxy, ProxyEvents())
        }

        authCodes.ifEnabled.addSource(authCodes.authCode) {
            authCodes.ifEnabled.value = it.length == 6
        }
    }

    inner class ProxyEvents {
        fun clickCancel(view: View) {
            dismiss()
        }

        fun clickConfirm(view: View) {
            confirmListener?.run {
                val authStr = authCodes.authCode.value
                val verifyResult = OtpUtils.verify(authStr ?: "")
                if (!verifyResult) {
                    MyToast.showToast("Authentication Failed :(")
                }
                this(verifyResult)
            }
            dismiss()
        }

        fun getTextWatcher(editable: Editable) {
            for (index in 0 until 6) {
                if (index < editable.length) {
                    authCodes.authList[index].value = editable[index].toString()
                } else {
                    authCodes.authList[index].value = ""
                }
            }
        }

    }

    override fun getMaxWidth(): Int {
        return ScreenUtils.getAppScreenWidth() - SizeUtils.dp2px(32f)
    }

}

data class AuthCodeList (
    val authCode: MutableLiveData<String> = MutableLiveData(""),
    val authList: MutableList<MutableLiveData<String>> = MutableList(6) { MutableLiveData("") },
    val ifEnabled: MediatorLiveData<Boolean> = MediatorLiveData(false)
)