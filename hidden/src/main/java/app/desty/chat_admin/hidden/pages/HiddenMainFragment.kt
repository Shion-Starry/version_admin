package app.desty.chat_admin.hidden.pages

import android.os.Bundle
import android.view.View
import app.desty.chat_admin.common.base.BaseVMFragment
import app.desty.chat_admin.common.base.DataBindingConfig
import app.desty.chat_admin.common.constants.RouteConstants
import app.desty.chat_admin.common.utils.MyToast
import app.desty.chat_admin.hidden.BR
import app.desty.chat_admin.hidden.R
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RouteConstants.Hidden.main)
class HiddenMainFragment : BaseVMFragment<HiddenMainViewModel>() {
    override fun initViewModel() {

    }

    override fun getDataBindingConfig(): DataBindingConfig =
        DataBindingConfig(R.layout.fragment_hidden_main, BR.mState, mState)
            .addBindingParam(BR.click, ClickEvents())

    override fun init(savedInstanceState: Bundle?) {

    }

    inner class ClickEvents {
        fun clickTesting(view: View) {
            MyToast.showToast("The output of the testing is ${mState.testInput}")
        }
    }

}